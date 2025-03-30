package com.sacavix.mq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.sacavix.mq.dummy.Pedido;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

@Slf4j
@Component
public class Consumer {

    // Almacén temporal de inventario
    private final Map<String, AtomicInteger> inventario = new ConcurrentHashMap<>();

    @RabbitListener(queues = { "${sacavix.queue.name}" })
    public void receive(@Payload Pedido pedido) {
        log.info("Pedido recibido: {}", pedido);
        
        // Actualizar el inventario
        inventario.computeIfAbsent(pedido.getProducto(), k -> new AtomicInteger(0))
                  .addAndGet(pedido.getCantidad());

        // Simular procesamiento lento
        procesarPedidoLentamente();
    }

    private void procesarPedidoLentamente() {
        try {
            Thread.sleep(5000);  // Simular retraso de procesamiento
        } catch (InterruptedException e) {
            log.error("Error durante el procesamiento", e);
        }
    }

    // Método para obtener el inventario actual
    public Map<String, Integer> getInventario() {
        Map<String, Integer> snapshot = new ConcurrentHashMap<>();
        inventario.forEach((producto, cantidad) -> snapshot.put(producto, cantidad.get()));
        return snapshot;
    }
}
