package com.sacavix.mq.dummy;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString

public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private UUID id;                // Identificador único del pedido
    private String producto;        // Nombre del producto
    private int cantidad;           // Cantidad del producto
    private double precioTotal;     // Precio total del pedido
    private LocalDate fecha;        // Fecha del pedido
}
