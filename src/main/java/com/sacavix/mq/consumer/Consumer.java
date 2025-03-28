package com.sacavix.mq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.sacavix.mq.dummy.Pedido;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Consumer {

    @RabbitListener(queues = { "${sacavix.queue.name}" })
    public void receive(@Payload Pedido pedido) {
        log.info("Pedido recibido: {}", pedido);

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
}
