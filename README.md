# Order Workflow Service

Technical assignment implemented with Java and Spring Boot 3.

The service exposes REST APIs to create and manage orders, enforcing valid state transitions throughout the order lifecycle.

## Stack

- Java 21
- Spring Boot 3.5.8
- Maven
- Spring Data JPA
- H2
- JUnit 5

## Assumptions

- The application is a single-tenant monolith.
- Authentication and authorization are outside the scope of the assignment unless explicitly required.

## Run locally

```bash
./mvnw spring-boot:run