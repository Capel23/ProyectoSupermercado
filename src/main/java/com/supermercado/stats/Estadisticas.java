// src/main/java/com/supermercado/stats/Estadisticas.java
package com.supermercado.stats;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Estadisticas {
    private final AtomicLong clientesGenerados = new AtomicLong(0);
    private final AtomicLong clientesAtendidos = new AtomicLong(0);
    private final AtomicLong clientesAbandonados = new AtomicLong(0);
    private final AtomicLong sumaTiemposEspera = new AtomicLong(0);
    private final AtomicLong sumaTiemposAtencion = new AtomicLong(0);
    private final ConcurrentHashMap<Integer, AtomicLong> clientesPorCaja = new ConcurrentHashMap<>();

    private final long instanteInicio;
    private long instanteFin;

    public Estadisticas() {
        this.instanteInicio = System.currentTimeMillis();
    }

    public void incrementarClientesGenerados() {
        clientesGenerados.incrementAndGet();
    }

    public void incrementarClientesAbandonados() {
        clientesAbandonados.incrementAndGet();
    }

    public void registrarInicioAtencion(long tiempoEsperaMillis) {
        sumaTiemposEspera.addAndGet(tiempoEsperaMillis);
    }

    public void registrarFinAtencion(long tiempoAtencionMillis) {
        sumaTiemposAtencion.addAndGet(tiempoAtencionMillis);
        clientesAtendidos.incrementAndGet();
    }

    public void registrarClienteAtendidoPorCaja(int idCaja) {
        clientesPorCaja.computeIfAbsent(idCaja, k -> new AtomicLong(0))
                .incrementAndGet();
    }

    public void finalizarSimulacion() {
        this.instanteFin = System.currentTimeMillis();
    }

    // Getters para el informe
    public long getClientesGenerados() { return clientesGenerados.get(); }
    public long getClientesAtendidos() { return clientesAtendidos.get(); }
    public long getClientesAbandonados() { return clientesAbandonados.get(); }
    public double getTiempoMedioEspera() {
        return clientesAtendidos.get() > 0 ?
                sumaTiemposEspera.get() / (double) clientesAtendidos.get() : 0;
    }
    public double getTiempoMedioAtencion() {
        return clientesAtendidos.get() > 0 ?
                sumaTiemposAtencion.get() / (double) clientesAtendidos.get() : 0;
    }
    public long getTiempoTotalSimulacion() {
        return instanteFin - instanteInicio;
    }
    public ConcurrentHashMap<Integer, AtomicLong> getClientesPorCaja() {
        return clientesPorCaja;
    }

    public void mostrarInforme() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("          INFORME FINAL DE SIMULACIÓN");
        System.out.println("=".repeat(60));
        System.out.printf("Tiempo total de simulación: %.2f s%n", getTiempoTotalSimulacion() / 1000.0);
        System.out.println("-".repeat(60));
        System.out.printf("Clientes generados:     %5d%n", getClientesGenerados());
        System.out.printf("Clientes atendidos:     %5d%n", getClientesAtendidos());
        System.out.printf("Clientes abandonados:   %5d%n", getClientesAbandonados());
        System.out.println("-".repeat(60));
        System.out.printf("Tiempo medio de espera: %.2f s%n", getTiempoMedioEspera() / 1000.0);
        System.out.printf("Tiempo medio atención:  %.2f s%n", getTiempoMedioAtencion() / 1000.0);
        System.out.println("-".repeat(60));
        System.out.println("Clientes atendidos por caja:");
        getClientesPorCaja().entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .forEach(entry ->
                        System.out.printf("  Caja %d -> %4d clientes%n",
                                entry.getKey(), entry.getValue().get()));
        System.out.println("=".repeat(60) + "\n");
    }
}