// src/main/java/com/supermercado/model/ConfiguracionSimulacion.java
package com.supermercado.model;

public class ConfiguracionSimulacion {
    public final int numCajas;
    public final int capacidadCola;
    public final long duracionSimulacionMillis;
    public final int minArticulos;
    public final int maxArticulos;
    public final int minIntervaloLlegada;
    public final int maxIntervaloLlegada;
    public final int tiempoAtencionPorArticulo; // milisegundos

    public ConfiguracionSimulacion(int numCajas, int capacidadCola, long duracionSimulacionMillis,
                                   int minArticulos, int maxArticulos,
                                   int minIntervaloLlegada, int maxIntervaloLlegada,
                                   int tiempoAtencionPorArticulo) {
        this.numCajas = numCajas;
        this.capacidadCola = capacidadCola;
        this.duracionSimulacionMillis = duracionSimulacionMillis;
        this.minArticulos = minArticulos;
        this.maxArticulos = maxArticulos;
        this.minIntervaloLlegada = minIntervaloLlegada;
        this.maxIntervaloLlegada = maxIntervaloLlegada;
        this.tiempoAtencionPorArticulo = tiempoAtencionPorArticulo;
    }

    // Método de fábrica para configuración por defecto
    public static ConfiguracionSimulacion configuracionPorDefecto() {
        return new ConfiguracionSimulacion(
                3,          // 3 cajas
                20,         // Capacidad cola
                30_000,     // 30 segundos de simulación
                5,          // Mínimo artículos
                30,         // Máximo artículos
                200,        // Mínimo intervalo llegada (ms)
                800,        // Máximo intervalo llegada (ms)
                100         // Tiempo por artículo (ms)
        );
    }
}