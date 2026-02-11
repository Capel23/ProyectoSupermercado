// src/main/java/com/supermercado/concurrency/GeneradorClientes.java
package com.supermercado.concurrency;

import com.supermercado.model.Cliente;
import com.supermercado.model.ConfiguracionSimulacion;
import com.supermercado.stats.Estadisticas;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class GeneradorClientes implements Runnable {
    private final ColaClientes colaClientes;
    private final Estadisticas estadisticas;
    private final ConfiguracionSimulacion config;
    private final AtomicLong contadorClientes = new AtomicLong(0);
    private volatile boolean ejecutando = true;
    private final Random random = new Random();

    public GeneradorClientes(ColaClientes colaClientes, Estadisticas estadisticas,
                             ConfiguracionSimulacion config) {
        this.colaClientes = colaClientes;
        this.estadisticas = estadisticas;
        this.config = config;
    }

    @Override
    public void run() {
        System.out.println("[GENERADOR] Iniciado - Generando clientes...");

        while (ejecutando) {
            // Generar cliente
            long id = contadorClientes.incrementAndGet();
            int numArticulos = random.nextInt(
                    config.maxArticulos - config.minArticulos + 1) + config.minArticulos;

            Cliente cliente = new Cliente(id, numArticulos);
            estadisticas.incrementarClientesGenerados();

            // Intentar añadir a la cola
            if (!colaClientes.agregarCliente(cliente)) {
                estadisticas.incrementarClientesAbandonados();
                System.out.printf("[GENERADOR] Cliente %d ABANDONÓ (cola llena)%n", id);
            } else {
                System.out.printf("[GENERADOR] Cliente %d añadido a cola (artículos: %d)%n",
                        id, numArticulos);
            }

            // Esperar intervalo aleatorio
            try {
                int intervalo = random.nextInt(
                        config.maxIntervaloLlegada - config.minIntervaloLlegada + 1)
                        + config.minIntervaloLlegada;
                Thread.sleep(intervalo);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("[GENERADOR] Detenido");
    }

    public void detener() {
        this.ejecutando = false;
    }
}