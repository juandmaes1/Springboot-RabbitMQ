package com.sacavix.mq.consumer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

@RestController
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private Consumer consumer;

    @GetMapping
    public Map<String, Integer> obtenerInventario() {
        return consumer.getInventario();
    }
}
