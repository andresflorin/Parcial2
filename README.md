# Banco de preguntas - Parcial 2

Autor: Andres Felipe Florin Burbano

Este proyecto es un microservicio desarrollado con Spring Boot para gestionar preguntas de opción múltiple dentro de un sistema de banco de preguntas. Permite crear preguntas validadas, almacenarlas en una base de datos en memoria y enviarlas a revisión mediante mensajería asíncrona con RabbitMQ.

## Descripción general

La aplicación expone una API REST para registrar preguntas con:

- texto de la pregunta
- justificación
- 4 opciones de respuesta
- opción correcta

Antes de guardar una pregunta, el servicio realiza una validación externa contra un microservicio en `http://localhost:8082/validate` y solo acepta la creación si la respuesta confirma que la justificación es válida.

Una vez creada, la pregunta queda en estado `DRAFT` y puede ser enviada a revisión mediante un mensaje en RabbitMQ. El listener consume ese evento y actualiza el estado de la pregunta a `PENDING_REVIEW`.

## Tecnologías usadas

- Java 21
- Spring Boot 4.1.2
- Spring Web
- Spring Data JPA
- H2 Database (memoria)
- Spring AMQP / RabbitMQ
- Maven

## Arquitectura

El proyecto combina una API REST con persistencia JPA y un patrón basado en estados para modelar la vida útil de una pregunta:

- `Question`: entidad principal
- `DraftState`: estado inicial de la pregunta
- `PendingReviewState`: estado de revisión pendiente
- `IQuestionState`: contrato del estado
- `IQuestionBuilder`: contrato del builder

También se utiliza una cola de mensajes para procesar la revisión de una pregunta de forma asíncrona:

- Exchange: `saberpro.direct.exchange`
- Queue: `question.submit.review.queue`
- Routing key: `question.submit`

## Requisitos previos

Antes de ejecutar el proyecto asegúrate de tener instalado:

- Java 21+
- Maven
- RabbitMQ ejecutándose localmente en el puerto por defecto `5672`
- El microservicio de validación disponible en `http://localhost:8082/validate`

## Configuración

La configuración principal está en `src/main/resources/application.properties`:

```properties
spring.application.name=bancopreguntasparcial2
server.port=8083
spring.datasource.url=jdbc:h2:mem:saberprodb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```

Esto configura:

- servidor en el puerto `8083`
- base de datos H2 en memoria
- consola H2 habilitada para inspección local

La consola H2 queda disponible en:

```text
http://localhost:8083/h2-console
```

JDBC URL sugerida:

```text
jdbc:h2:mem:saberprodb
```

## Ejecución del proyecto

1. Clona el repositorio.
2. Abre una terminal en la raíz del proyecto.
3. Ejecuta:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

En Windows PowerShell o CMD puedes usar:

```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

Si todo sale bien, la aplicación quedará levantada en:

```text
http://localhost:8083
```

## API REST

### 1. Crear una pregunta

Endpoint:

```http
POST /questions
```

Ejemplo de cuerpo JSON:

```json
{
  "text": "¿Cuál es la capital de Colombia?",
  "justification": "La capital de Colombia es Bogotá y es la sede del gobierno nacional.",
  "options": [
    "Bogotá",
    "Medellín",
    "Cali",
    "Barranquilla"
  ],
  "correctOption": "Bogotá"
}
```

Resultado esperado:

- `201 Created` si la validación externa acepta la pregunta.
- `400 Bad Request` si la validación falla o los datos no cumplen la validación de negocio.

### 2. Enviar una pregunta a revisión

Endpoint:

```http
POST /questions/{id}/submit
```

Ejemplo:

```http
POST /questions/1/submit
```

Respuesta:

```json
{
  "operationId": "<uuid>"
}
```

Este endpoint no bloquea la operación; publica un mensaje en RabbitMQ para que el listener procese la transición de estado en segundo plano.

## Flujo de estados

La pregunta inicia en `DRAFT` y puede avanzar a `PENDING_REVIEW` al ejecutar la acción de envío para revisión.

```text
DRAFT --> PENDING_REVIEW
```

La transición se realiza desde el listener de RabbitMQ:

```java
question.getState().next(question);
repository.save(question);
```

## Validación externa

La creación de preguntas depende de un servicio externo accesible en `http://localhost:8082/validate`.

Se espera una respuesta HTTP 200 con un payload similar a:

```json
{
  "valid": true
}
```

Si el servicio no responde o devuelve un valor inválido, la creación de la pregunta es rechazada.

## Estructura del proyecto

```text
src/
├── main/
│   ├── java/
│   │   └── com/unicauca/edu/bancopreguntasparcial2/
│   │       ├── Bancopreguntasparcial2Application.java
│   │       ├── domain/
│   │       │   └── model/
│   │       │       ├── DraftState.java
│   │       │       ├── IQuestionBuilder.java
│   │       │       ├── IQuestionState.java
│   │       │       ├── PendingReviewState.java
│   │       │       └── Question.java
│   │       └── infrastructure/
│   │           ├── api/
│   │           │   ├── QuestionController.java
│   │           │   └── QuestionRequestDTO.java
│   │           ├── config/
│   │           │   └── RabbitConfig.java
│   │           ├── jpa/
│   │           │   ├── mapper/
│   │           │   ├── record/
│   │           │   └── repository/
│   │           └── messaging/
│   │               └── QuestionListener.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/unicauca/edu/bancopreguntasparcial2/
            └── Bancopreguntasparcial2ApplicationTests.java
```

## Observaciones

- La base de datos es en memoria, por lo que los datos se reinician al detener la aplicación.
- El proyecto está listo para pruebas de laboratorio o demostración de un flujo de creación y revisión de preguntas.
- La validación y la revisión se manejan como componentes desacoplados para mantener una arquitectura más flexible.

## Licencia

Este proyecto se entrega como material académico para fines de evaluación y desarrollo universitario.
