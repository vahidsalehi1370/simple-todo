# Implementation Plan: Simple Todo REST API

**Branch**: `001-simple-todo-app` | **Date**: 2026-10-25 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification plus user-selected implementation constraints: Java 21, Spring Boot REST API, in-memory storage, Controller/Service/Repository layers, and JUnit.

## Summary

Build a small REST API for creating, listing, and completing Todo tasks. Use a Java 21 Spring Boot MVC application with a Task model and TODO/DONE enum. A service enforces title validation and task state rules; a repository stores tasks in memory and seeds at least three examples on startup. No database, authentication, frontend, or extra architecture is included.

## Technical Context

**Language/Version**: Java 21

**Primary Dependencies**: Spring Boot 4.1.x; `spring-boot-starter-webmvc` and `spring-boot-starter-test`

**Storage**: In-memory repository; all tasks reset when the application stops

**Testing**: JUnit Jupiter through `spring-boot-starter-test`; service unit tests and REST integration tests covering status codes, response fields, and Persian error messages for validation, malformed JSON, invalid id syntax, and unknown task ids

**Target Platform**: Java server process exposing an HTTP REST API

**Project Type**: Single web-service project

**Performance Goals**: No performance, availability, or capacity SLA is required for this learning application

**Constraints**: No database, login, or authentication; Persian user-facing messages; validation and business rules enforced in the application service; keep dependencies and structure minimal

**Scale/Scope**: One active application instance, one in-memory task list, at least three tasks seeded at startup, and the three user flows in the specification; concurrent request behavior is out of scope

**Build Tool**: Maven with Maven Wrapper, selected as a conventional, beginner-friendly default because the repository has no existing build configuration

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Approved specification before implementation**: PASS. The active specification defines the user flows, business rules, and acceptance scenarios.
- **Testable behavior**: PASS. Each REST operation and validation/error outcome has an observable contract and JUnit coverage.
- **Business rules enforced in the application**: PASS. The service validates titles, sets initial status, enforces allowed transitions, and handles idempotent completion; the controller maps outcomes to HTTP responses.
- **Simplicity and maintainability**: PASS. One application, three requested layers, two runtime concerns (web MVC and in-memory storage), and no database or mapping framework.
- **Plan and tasks before implementation**: DESIGN PASS. This plan precedes implementation. Implementation remains gated on `/speckit-tasks` producing reviewed tasks.

No constitutional violations require a complexity exception.

**Post-design re-check**: PASS. The data model, REST contract, and quickstart preserve the approved scope and business rules. The implementation gate remains closed until `/speckit-tasks` produces reviewed tasks.

## Project Structure

### Documentation (this feature)

```text
specs/001-simple-todo-app/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── rest-api.md
└── tasks.md                 # Created by /speckit-tasks, before implementation
```

### Source Code (repository root)

```text
pom.xml
mvnw
mvnw.cmd
src/
├── main/
│   ├── java/com/example/todo/
│   │   ├── TodoApplication.java
│   │   └── task/
│   │       ├── Task.java
│   │       ├── TaskStatus.java
│   │       ├── TaskController.java
│   │       ├── TaskService.java
│   │       ├── TaskRepository.java
│   │       ├── InMemoryTaskRepository.java
│   │       ├── CreateTaskRequest.java
│   │       ├── ApiError.java
│   │       └── ApiExceptionHandler.java
└── test/
    └── java/com/example/todo/task/
        ├── TaskServiceTest.java
        └── TaskApiIntegrationTest.java
```

**Structure Decision**: Use one Maven application and a single `task` package. Keep the three requested layers explicit; the repository interface has one in-memory implementation. Use small request/error records only where they prevent clients from setting server-owned fields or provide consistent Persian error responses. No frontend project or persistence module is needed.

## Complexity Tracking

No violations. The explicitly requested Repository layer remains a small in-memory abstraction and introduces no database or persistence framework.
