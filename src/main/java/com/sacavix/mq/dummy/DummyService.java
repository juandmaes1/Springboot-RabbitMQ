package com.sacavix.mq.dummy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sacavix.mq.publisher.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DummyService {

    private static final Logger log = LoggerFactory.getLogger(DummyService.class);

    @Autowired
    private Publisher publisher;

    public void sendToRabbit(Pedido pedido) {
        log.info("El pedido '{}' será enviado...", pedido);
        this.publisher.send(pedido);
    }
}
