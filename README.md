# 🛒 Simulador de Supermercado

Un simulador de supermercado implementado en Java que utiliza programación concurrente para simular el flujo de clientes en un supermercado con múltiples cajas de pago.

## 📋 Descripción

Este proyecto simula el funcionamiento de un supermercado donde:
- Los clientes llegan aleatoriamente y hacen cola
- Múltiples cajas atienden a los clientes de manera concurrente
- Se registran estadísticas detalladas del proceso
- El sistema maneja la sincronización entre hilos de manera eficiente

## 🔧 Tecnologías

- **Java 21** - Lenguaje principal
- **Maven** - Gestión de dependencias y construcción
- **Concurrencia Java** - ExecutorService, Thread pools, sincronización

## 🚀 Instalación y Ejecución

### Prerrequisitos
- JDK 21 o superior
- Maven 3.6+

### Compilar y Ejecutar

```bash
# Compilar el proyecto
mvn compile

# Ejecutar la simulación
mvn exec:java -Dexec.mainClass="com.supermercado.SimuladorSupermercado"
```

O compilar y ejecutar directamente:

```bash
mvn clean compile exec:java -Dexec.mainClass="com.supermercado.SimuladorSupermercado"
```

## ⚙️ Configuración

El simulador permite personalizar varios parámetros editando la configuración en `SimuladorSupermercado.java`:

```java
ConfiguracionSimulacion config = new ConfiguracionSimulacion(
    4,          // Número de cajas
    30,         // Capacidad máxima de la cola
    60_000,     // Duración de la simulación (ms)
    3,          // Mínimo artículos por cliente
    50,         // Máximo artículos por cliente
    100,        // Intervalo mínimo entre llegadas (ms)
    600,        // Intervalo máximo entre llegadas (ms)
    80          // Tiempo de atención por artículo (ms)
);
```

### Configuración por defecto
- **2 cajas** de cobro
- **Cola con capacidad para 20 clientes**
- **30 segundos** de simulación
- **1-20 artículos** por cliente
- **500-2000ms** de intervalo entre llegadas
- **100ms** por artículo para procesar

## 🏗️ Arquitectura

### Estructura del Proyecto

```
src/main/java/com/supermercado/
├── SimuladorSupermercado.java      # Clase principal
├── concurrency/
│   ├── Caja.java                  # Hilo que representa una caja
│   ├── ColaClientes.java          # Cola thread-safe de clientes
│   └── GeneradorClientes.java     # Generador de clientes aleatorios
├── model/
│   ├── Cliente.java               # Modelo de cliente
│   └── ConfiguracionSimulacion.java # Parámetros de configuración
└── stats/
    └── Estadisticas.java          # Recolección de estadísticas
```

### Componentes Principales

1. **SimuladorSupermercado**: Coordina toda la simulación
2. **Caja**: Worker threads que procesan clientes de la cola
3. **ColaClientes**: Cola thread-safe usando `BlockingQueue`
4. **GeneradorClientes**: Produce clientes aleatoriamente
5. **Estadisticas**: Recolecta métricas de rendimiento

## 📊 Métricas

El simulador genera un informe completo que incluye:

- 📈 **Estadísticas de clientes**: total procesados, rechazados
- ⏱️ **Tiempos de espera**: promedio, máximo, mínimo
- 🏪 **Eficiencia de cajas**: clientes atendidos por caja
- 📉 **Ocupación de cola**: máxima, promedio
- ⚡ **Throughput**: clientes por segundo

## 🔄 Conceptos de Concurrencia

Este proyecto demuestra:

- **Thread pools** con `ExecutorService`
- **Colas bloqueantes** (`BlockingQueue`)
- **Sincronización de hilos** 
- **Patrón Productor-Consumidor**
- **Gestión ordenada de recursos**
- **Manejo de interrupciones**

## 🎯 Casos de Uso

Ideal para:
- 📚 **Aprendizaje de concurrencia en Java**
- 🧪 **Testing de sistemas multi-hilo** 
- 📐 **Modelado de sistemas de colas**
- ⚖️ **Análisis de balanceamento de carga**

## 🤝 Contribuciones

¿Ideas para mejoras? ¡Son bienvenidas!

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

---