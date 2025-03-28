package com.sacavix.mq.dummy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class DummyController {

    @Autowired
    private DummyService dummyService;

    private final List<String> productos = List.of("Laptop", "Smartphone", "Tablet", "Monitor", "Teclado", "Mouse", "Auriculares");

    @GetMapping
    public void testSendPedido() {
        // Crear un pedido aleatorio
        UUID id = UUID.randomUUID();
        String producto = productos.get(ThreadLocalRandom.current().nextInt(productos.size()));
        int cantidad = ThreadLocalRandom.current().nextInt(1, 5);  // Cantidad entre 1 y 5
        double precioTotal = cantidad * (100 + ThreadLocalRandom.current().nextDouble(500));  // Precio aleatorio entre 100 y 600 por unidad
        LocalDate fecha = LocalDate.now();

        Pedido pedido = new Pedido(id, producto, cantidad, precioTotal, fecha);

        dummyService.sendToRabbit(pedido);
    }
}
