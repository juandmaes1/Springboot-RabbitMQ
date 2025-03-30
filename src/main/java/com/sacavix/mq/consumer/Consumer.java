package com.sacavix.mq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.sacavix.mq.dummy.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

@Component
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    private final Map<String, AtomicInteger> inventario = new ConcurrentHashMap<>();

    @RabbitListener(queues = { "${sacavix.queue.name}" })
    public void receive(@Payload Pedido pedido) {
        log.info("Pedido recibido: {}", pedido);

        inventario.computeIfAbsent(pedido.getProducto(), k -> new AtomicInteger(0))
                  .addAndGet(pedido.getCantidad());

        procesarPedidoLentamente();
    }

    private void procesarPedidoLentamente() {
        try {
            Thread.sleep(5000);
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
