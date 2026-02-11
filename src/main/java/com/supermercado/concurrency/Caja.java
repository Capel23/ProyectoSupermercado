// src/main/java/com/supermercado/concurrency/Caja.java
package com.supermercado.concurrency;

import com.supermercado.model.Cliente;
import com.supermercado.model.ConfiguracionSimulacion;
import com.supermercado.stats.Estadisticas;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Caja implements Runnable {
    private final int id;
    private final ColaClientes colaClientes;
    private final Estadisticas estadisticas;
    private final ConfiguracionSimulacion config;
    private final AtomicInteger clientesAtendidos = new AtomicInteger(0);
    private volatile boolean ejecutando = true;
    private final Random random = new Random();

    public Caja(int id, ColaClientes colaClientes, Estadisticas estadisticas,
                ConfiguracionSimulacion config) {
        this.id = id;
        this.colaClientes = colaClientes;
        this.estadisticas = estadisticas;
        this.config = config;
    }

    @Override
    public void run() {
        System.out.printf("[CAJA %d] Iniciada%n", id);

        while (ejecutando || !colaClientes.estaVacia()) {
            try {
                // Obtener cliente de la cola (bloqueante)
                Cliente cliente = colaClientes.obtenerCliente();

                // Registrar inicio de atención
                cliente.setInstanteInicioAtencion(Instant.now());
                clientesAtendidos.incrementAndGet();
                estadisticas.registrarInicioAtencion(cliente.getTiempoEsperaMillis());

                System.out.printf("[CAJA %d] Atendiendo %s (espera: %.2f s)%n",
                        id, cliente, cliente.getTiempoEsperaMillis() / 1000.0);

                // Simular tiempo de atención (proporcional a artículos)
                int tiempoAtencion = cliente.getNumArticulos() * config.tiempoAtencionPorArticulo;
                // Añadir variabilidad del 10%
                tiempoAtencion += random.nextInt(tiempoAtencion / 10) - (tiempoAtencion / 20);
                tiempoAtencion = Math.max(50, tiempoAtencion); // mínimo 50ms

                Thread.sleep(tiempoAtencion);

                // Registrar fin de atención
                cliente.setInstanteSalida(Instant.now());
                estadisticas.registrarFinAtencion(cliente.getTiempoAtencionMillis());
                estadisticas.registrarClienteAtendidoPorCaja(id);

                System.out.printf("[CAJA %d] Cliente %d ATENDIDO (tiempo: %.2f s)%n",
                        id, cliente.getId(), cliente.getTiempoAtencionMillis() / 1000.0);

            } catch (InterruptedException e) {
                // Permitir salida limpia al interrumpir
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.printf("[CAJA %d] Error: %s%n", id, e.getMessage());
            }
        }

        System.out.printf("[CAJA %d] Detenida (clientes atendidos: %d)%n",
                id, clientesAtendidos.get());
    }

    public void detener() {
        this.ejecutando = false;
    }

    public int getClientesAtendidos() {
        return clientesAtendidos.get();
    }
}