# Comunicación entre Microservicios usando RabbitMQ como Broker de Mensajes

## Descripción del Proyecto

Este proyecto demuestra cómo un microservicio **Productor** y un microservicio **Consumidor** intercambian mensajes de forma asíncrona usando **RabbitMQ** como **message broker** en una aplicación **Spring Boot** que busca simular la gestion de pedidos y de inventarios.

- El **Productor** expone un endpoint REST que recibe órdenes (`Pedido`) y las publica en una cola de RabbitMQ.
- El **Consumidor** escucha dicha cola y actualiza un inventario en memoria, ofreciendo otro endpoint para consultar su estado.
- Se incluye manejo de **Circuit Breaker** con Resilience4j para tolerancia a fallos.

---

## Estructura del Proyecto

```plaintext
Springboot-RabbitMQ-main/
├── pom.xml
├── README.md                      # Documentación del proyecto
├── mvnw, mvnw.cmd, .mvn/          # Wrapper de Maven
└── src
    ├── main
    │   ├── java/com/sacavix/mq
    │   │   ├── SpringBootRabbitMqApplication.java  # Clase principal
    │   │   ├── consumer
    │   │   │   ├── Consumer.java                    # Lógica de escucha y procesamiento
    │   │   │   └── InventarioController.java        # Endpoint GET /inventario
    │   │   ├── dummy
    │   │   │   ├── DummyController.java             # Endpoint POST /test
    │   │   │   ├── DummyService.java                # Servicio para envío a Rabbit
    │   │   │   └── Pedido.java                      # DTO de mensaje
    │   │   └── publisher
    │   │       ├── Publisher.java                  # Componente de envío
    │   │       └── PublisherConfig.java            # Configuración de cola
    │   └── resources
    │       ├── application.properties              # Configuración de RabbitMQ y Resilience4j
    │       └── static
    │           ├── test.html                       # UI para enviar pedidos
    │           └── inventario.html                 # UI para ver inventario
    └── test/java/com/sacavix/mq
        └── SpringBootRabbitMqApplicationTests.java # Pruebas unitarias
```

---

## Dependencias Principales

En **pom.xml** se incluyen:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>io.github.resilience4j</groupId>
  <artifactId>resilience4j-spring-boot2</artifactId>
</dependency>
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <optional>true</optional>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
</dependency>
```

---

## Configuración de RabbitMQ

Por defecto, la aplicación usa las siguientes propiedades (en `application.properties`):

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

sacavix.queue.name=cola1
```
La consola de administración de RabbitMQ estará disponible en http://localhost:15672 (guest/guest).

---

## Endpoints HTTP

- **POST /test**  
  Envía un `Pedido` al microservicio Productor:
  ```bash
  curl -X POST http://localhost:8080/test \
       -H "Content-Type: application/json" \
       -d '{"producto":"Tablet","cantidad":2}'
  ```
  Responde con el nombre y cantidad del pedido o error si el Circuit Breaker está abierto.
  --> Para terminos practicos y de simulación el Circuit Breaker toma como un error la recepcion de una 'laptop' como tipo de producto.

- **GET /inventario**  
  Obtiene el inventario actualizado por el Consumidor:
  ```bash
  curl http://localhost:8080/inventario
  ```

Se puede acceder a las interfaces (UI's) en:
- http://localhost:8080/test.html
- http://localhost:8080/inventario.html

---

## Diagrama de Arquitectura

![Arquitectura RabbitMQ Microservicios](architecture_rabbitmq.jpg)

---

## Explicación de la Implementación

- **`SpringBootRabbitMqApplication`**  
  Punto de entrada de Spring Boot.

- **`PublisherConfig`**  
  Define la `Queue` usando la propiedad `sacavix.queue.name`.

- **`Publisher`**  
  Componente que publica mensajes en la cola con `RabbitTemplate`.

- **`DummyController`**  
  Expone el endpoint **POST /test** que crea un `Pedido` y lo envía.

- **`DummyService`**  
  Encapsula la lógica de envío al `Publisher`.

- **`Pedido`**  
  DTO serializable con `id`, `producto`, `cantidad`, `precioTotal` y `fecha`.

- **`Consumer`**  
  Escucha la cola con `@RabbitListener`, procesa cada `Pedido` y actualiza un inventario concurrente.

- **`InventarioController`**  
  Expone el endpoint **GET /inventario** para consultar el inventario.

## Contribuciones
Este proyecto fue desarrollado por:
- Laura Camila Rodriguez León
- Jorge Esteban Diaz Bernal
- Juan Diego Martinez Escobar
