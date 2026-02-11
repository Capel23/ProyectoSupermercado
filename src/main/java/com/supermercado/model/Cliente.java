// src/main/java/com/supermercado/model/Cliente.java
package com.supermercado.model;

import java.time.Instant;

public class Cliente {
    private final long id;
    private final int numArticulos;
    private final Instant instanteLlegada;
    private Instant instanteInicioAtencion;
    private Instant instanteSalida;

    public Cliente(long id, int numArticulos) {
        this.id = id;
        this.numArticulos = numArticulos;
        this.instanteLlegada = Instant.now();
    }

    // Getters y setters
    public long getId() { return id; }
    public int getNumArticulos() { return numArticulos; }
    public Instant getInstanteLlegada() { return instanteLlegada; }
    public Instant getInstanteInicioAtencion() { return instanteInicioAtencion; }
    public void setInstanteInicioAtencion(Instant instanteInicioAtencion) {
        this.instanteInicioAtencion = instanteInicioAtencion;
    }
    public Instant getInstanteSalida() { return instanteSalida; }
    public void setInstanteSalida(Instant instanteSalida) {
        this.instanteSalida = instanteSalida;
    }

    public long getTiempoEsperaMillis() {
        return instanteInicioAtencion != null ?
                instanteInicioAtencion.toEpochMilli() - instanteLlegada.toEpochMilli() : 0;
    }

    public long getTiempoAtencionMillis() {
        return instanteSalida != null ?
                instanteSalida.toEpochMilli() - instanteInicioAtencion.toEpochMilli() : 0;
    }

    @Override
    public String toString() {
        return String.format("Cliente[id=%d, artículos=%d]", id, numArticulos);
    }
}