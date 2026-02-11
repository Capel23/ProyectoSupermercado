// src/main/java/com/supermercado/SimuladorSupermercado.java
package com.supermercado;

import com.supermercado.concurrency.*;
import com.supermercado.model.ConfiguracionSimulacion;
import com.supermercado.stats.Estadisticas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimuladorSupermercado {
    private final ConfiguracionSimulacion config;
    private final ColaClientes colaClientes;
    private final Estadisticas estadisticas;

    public SimuladorSupermercado(ConfiguracionSimulacion config) {
        this.config = config;
        this.colaClientes = new ColaClientes(config.capacidadCola);
        this.estadisticas = new Estadisticas();
    }

    public void ejecutar() throws InterruptedException {
        System.out.println("=".repeat(60));
        System.out.println("  SIMULADOR DE SUPERMERCADO - SISTEMA MULTIPROCESO");
        System.out.println("=".repeat(60));
        System.out.printf("Configuración:%n");
        System.out.printf("  - Cajas: %d%n", config.numCajas);
        System.out.printf("  - Capacidad cola: %d%n", config.capacidadCola);
        System.out.printf("  - Duración: %.1f segundos%n", config.duracionSimulacionMillis / 1000.0);
        System.out.printf("  - Artículos por cliente: %d-%d%n", config.minArticulos, config.maxArticulos);
        System.out.printf("  - Intervalo llegadas: %d-%d ms%n", config.minIntervaloLlegada, config.maxIntervaloLlegada);
        System.out.println("=".repeat(60) + "\n");

        // Crear Executor para las cajas
        ExecutorService executorCajas = Executors.newFixedThreadPool(config.numCajas);
        List<Caja> cajas = new ArrayList<>();

        // Iniciar cajas
        for (int i = 1; i <= config.numCajas; i++) {
            Caja caja = new Caja(i, colaClientes, estadisticas, config);
            cajas.add(caja);
            executorCajas.submit(caja);
        }

        // Iniciar generador de clientes en hilo separado
        GeneradorClientes generador = new GeneradorClientes(colaClientes, estadisticas, config);
        Thread hiloGenerador = new Thread(generador);
        hiloGenerador.start();

        // Esperar tiempo de simulación
        Thread.sleep(config.duracionSimulacionMillis);

        // Detener generador
        generador.detener();
        hiloGenerador.join(1000); // Esperar máximo 1 segundo

        // Esperar a que se vacíe la cola (máximo 5 segundos adicionales)
        System.out.println("\n[MAIN] Tiempo de simulación finalizado. Vaciamiento de cola en curso...");
        long tiempoEsperaVaciamiento = 5000;
        long inicioVaciamiento = System.currentTimeMillis();

        while (!colaClientes.estaVacia() &&
                (System.currentTimeMillis() - inicioVaciamiento) < tiempoEsperaVaciamiento) {
            Thread.sleep(100);
        }

        // Detener cajas
        cajas.forEach(Caja::detener);

        // Apagar executor de forma ordenada
        executorCajas.shutdown();
        if (!executorCajas.awaitTermination(3, TimeUnit.SECONDS)) {
            executorCajas.shutdownNow();
        }

        // Finalizar estadísticas
        estadisticas.finalizarSimulacion();

        // Mostrar informe
        estadisticas.mostrarInforme();
    }

    public static void main(String[] args) {
        try {
            // Configuración personalizable
            ConfiguracionSimulacion config = ConfiguracionSimulacion.configuracionPorDefecto();

            // Para pruebas más intensivas, descomentar:
            /*
            config = new ConfiguracionSimulacion(
                4,          // 4 cajas
                30,         // Capacidad cola 30
                60_000,     // 60 segundos
                3,          // Mínimo artículos
                50,         // Máximo artículos
                100,        // Mínimo intervalo
                600,        // Máximo intervalo
                80          // Tiempo por artículo
            );
            */

            SimuladorSupermercado simulador = new SimuladorSupermercado(config);
            simulador.ejecutar();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Simulación interrumpida: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error durante la simulación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}