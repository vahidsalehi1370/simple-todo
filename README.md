# Simple Todo Application

A simple Todo application built with Spring Boot using a Spec-Driven Development approach.

## Overview

This project is a basic Todo application that allows users to:

- Create tasks
- View all tasks
- Mark tasks as completed
- Delete tasks

The project was implemented using a specification-first development workflow with Spec Kit.

## Technologies

- Java
- Spring Boot
- Spring Web
- Maven
- Spec Kit
- REST API

## Project Structure

```text
simple-todo/
├── .github/
├── .mvn/
├── .specify/
├── specs/
│   └── 001-simple-todo-app/
├── src/
│   ├── main/
│   └── test/
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## Running the Application

### Windows

```bash
mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

Alternatively, if Maven is installed:

```bash
mvn spring-boot:run
```

## Build

To build the project:

```bash
mvn clean package
```

or:

```bash
./mvnw clean package
```

## API

The Todo API provides endpoints for managing tasks.

Example base path:

```text
/api/tasks
```

Typical operations include:

```text
POST   /api/tasks
GET    /api/tasks
PATCH  /api/tasks/{id}/done
DELETE /api/tasks/{id}
```

## Spec-Driven Development

The application was developed using a specification-driven approach.

Project specifications are available under:

```text
specs/001-simple-todo-app/
```

The `.specify` directory contains the configuration and supporting files used by the Spec Kit workflow.

## Author

Developed as a simple example of building a Spring Boot application using Spec-Driven Development.
