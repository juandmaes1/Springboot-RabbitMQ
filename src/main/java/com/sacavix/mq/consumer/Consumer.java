package com.sacavix.mq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.sacavix.mq.dummy.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

@Component
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    // Almacén de inventario en memoria
    private final Map<String, AtomicInteger> inventario = new ConcurrentHashMap<>();

    // Circuit Breaker activado sobre el método
    @RabbitListener(queues = { "${sacavix.queue.name}" })
    @CircuitBreaker(name = "rabbitConsumerCB", fallbackMethod = "fallbackPedido")
    public void receive(@Payload Pedido pedido) {
        log.info("Pedido recibido: {}", pedido);

        // Simulación de fallo: si el producto es "Laptop", lanza error
        if ("Laptop".equalsIgnoreCase(pedido.getProducto())) {
            throw new RuntimeException("Simulación de error al procesar Laptop");
        }

        // Actualizar inventario
        inventario.computeIfAbsent(pedido.getProducto(), k -> new AtomicInteger(0))
                  .addAndGet(pedido.getCantidad());

        procesarPedidoLentamente();  // Simulación de procesamiento lento
    }

    // Método fallback si el Circuit Breaker se abre
    public void fallbackPedido(Pedido pedido, Throwable t) {
        log.error("⚠️ Circuit Breaker ACTIVADO. Pedido NO procesado: {}", pedido, t);
    }

    private void procesarPedidoLentamente() {
        try {
            Thread.sleep(5000); // Simula procesamiento demorado
        } catch (InterruptedException e) {
            log.error("Error durante el procesamiento", e);
        }
    }

    public Map<String, Integer> getInventario() {
        Map<String, Integer> snapshot = new ConcurrentHashMap<>();
        inventario.forEach((producto, cantidad) -> snapshot.put(producto, cantidad.get()));
        return snapshot;
    }
}
