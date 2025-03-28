package com.sacavix.mq.dummy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sacavix.mq.publisher.Publisher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DummyService {

    @Autowired
    private Publisher publisher;

    public void sendToRabbit(Pedido pedido) {
        log.info("El pedido '{}' será enviado...", pedido);
        this.publisher.send(pedido);
    }
}
