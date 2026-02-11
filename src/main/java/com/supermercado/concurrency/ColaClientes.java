// src/main/java/com/supermercado/concurrency/ColaClientes.java
package com.supermercado.concurrency;

import com.supermercado.model.Cliente;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ColaClientes {
    private final BlockingQueue<Cliente> cola;
    private final int capacidadMaxima;

    public ColaClientes(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.cola = new LinkedBlockingQueue<>(capacidadMaxima);
    }

    public boolean agregarCliente(Cliente cliente) {
        return cola.offer(cliente); // offer() no bloquea, devuelve false si está llena
    }

    public Cliente obtenerCliente() throws InterruptedException {
        return cola.take(); // take() bloquea hasta que haya un elemento
    }

    public int getTamañoActual() {
        return cola.size();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
}