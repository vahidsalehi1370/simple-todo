# Research: Simple Todo REST API

## Java and Spring Boot

- **Decision**: Use Java 21 with the supported Spring Boot 4.1.x line; let the project setup select the latest compatible patch.
- **Rationale**: The official Spring Boot requirements list Java 21 as supported, and the supported-versions page lists 4.1.x as an actively supported line.
- **Alternatives considered**: Spring Boot 3.x was not selected because the current supported line is newer and supports Java 21. A specific patch is intentionally not pinned in this design document.
- **Sources**: [Spring Boot system requirements](https://docs.spring.io/spring-boot/system-requirements.html); [Spring Boot support](https://spring.io/projects/spring-boot#support)

## REST Dependency

- **Decision**: Use `spring-boot-starter-webmvc` for the REST API.
- **Rationale**: This feature needs the conventional Spring MVC request/response model and has no reactive or streaming requirements.
- **Alternatives considered**: WebFlux was rejected because it introduces a different programming model without a requirement that benefits from it.
- **Source**: [Spring Boot build systems and starters](https://docs.spring.io/spring-boot/reference/using/build-systems.html)

## Validation and Business Rules

- **Decision**: Validate blank titles and task transitions in `TaskService`; do not add a validation starter for this scope.
- **Rationale**: The constitution requires business rules in the application, and direct service validation is sufficient for one required rule. The service remains the authoritative boundary even if a client bypasses the REST controller.
- **Alternatives considered**: Bean Validation annotations and `spring-boot-starter-validation` were not selected because they add a separate dependency and are unnecessary for this single rule.
- **Source**: [Spring Boot validation](https://docs.spring.io/spring-boot/reference/io/validation.html)

## Tests

- **Decision**: Use `spring-boot-starter-test` in test scope for JUnit Jupiter. Cover service rules with unit tests and HTTP behavior with a Spring Boot integration test.
- **Rationale**: This provides the requested JUnit support and verifies both application rules and the public REST behavior without adding a separate test framework.
- **Alternatives considered**: Separate JUnit dependencies were rejected because the Spring Boot test starter manages the common testing libraries. A dedicated MVC slice-test starter is unnecessary for the planned full application integration test.
- **Source**: [Spring Boot testing](https://docs.spring.io/spring-boot/reference/testing/index.html)

## In-Memory Repository

- **Decision**: Define the requested small `TaskRepository` interface and implement it with an in-memory Java collection. Seed at least three tasks when the application starts.
- **Rationale**: This keeps storage replaceable at a simple boundary while avoiding any database, Spring Data, or persistence configuration.
- **Alternatives considered**: Directly storing data in the controller or service was rejected because the user explicitly requested a Repository layer. Spring Data and database-backed repositories were rejected because persistence is out of scope.

## Build Tool

- **Decision**: Use Maven with the Maven Wrapper.
- **Rationale**: The repository has no build tool configured. Maven Wrapper makes the project runnable without requiring a separately installed Maven version and is a familiar learning default.
- **Alternatives considered**: Gradle is also viable but has no project-specific advantage here; using a system-installed build tool would make setup less reproducible.
