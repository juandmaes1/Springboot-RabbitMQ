package com.sacavix.mq.dummy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class DummyController {

    @Autowired
    private DummyService dummyService;

    private final List<String> productos = List.of("Laptop", "Smartphone", "Tablet", "Monitor", "Teclado", "Mouse", "Auriculares");

    @PostMapping
    public ResponseEntity<String> enviarPedidoDesdeFormulario(@RequestBody Pedido pedido) {
        UUID id = UUID.randomUUID();
        LocalDate fecha = LocalDate.now();
        double precioUnitario = 100 + Math.random() * 500;
        double precioTotal = precioUnitario * pedido.getCantidad();

        Pedido pedidoCompleto = new Pedido(id, pedido.getProducto(), pedido.getCantidad(), precioTotal, fecha);

        try {
            dummyService.sendToRabbit(pedidoCompleto);
            return ResponseEntity.ok(pedidoCompleto.getProducto() + " x" + pedidoCompleto.getCantidad());
        } catch (Exception e) {
            String error = e.getMessage();
            if (error != null && error.contains("CallNotPermittedException")) {
                return ResponseEntity.status(503).body("Circuit Breaker ACTIVADO: No se permitió procesar el pedido de " + pedido.getProducto());
            } else {
                return ResponseEntity.status(500).body("Error al enviar " + pedido.getProducto() + ": " + error);
            }
        }
    }


}
