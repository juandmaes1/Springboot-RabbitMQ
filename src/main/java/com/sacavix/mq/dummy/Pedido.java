package com.sacavix.mq.dummy;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String producto;
    private int cantidad;
    private double precioTotal;
    private LocalDate fecha;

    // Constructor vacío requerido por Spring
    public Pedido() {}

    public Pedido(UUID id, String producto, int cantidad, double precioTotal, LocalDate fecha) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
        this.fecha = fecha;
    }

    public UUID getId() { return id; }
    public String getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioTotal() { return precioTotal; }
    public LocalDate getFecha() { return fecha; }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", producto='" + producto + '\'' +
                ", cantidad=" + cantidad +
                ", precioTotal=" + precioTotal +
                ", fecha=" + fecha +
                '}';
    }
}
