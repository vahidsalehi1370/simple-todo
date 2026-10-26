# Simple Todo Application

A simple Todo application built with Spring Boot using a Spec-Driven Development approach.

## Overview

This project is a basic Todo application for managing tasks.

Main features:

- Create tasks
- View tasks
- Complete tasks
- Delete tasks

## Technologies

- Java
- Spring Boot
- Maven
- REST API
- Spec Kit
- GitHub

## Project Structure

```text
simple-todo/
├── .github/
│   └── skills/
├── .mvn/
│   └── wrapper/
├── .specify/
├── specs/
│   └── 001-simple-todo-app/
├── src/
│   ├── main/
│   └── test/
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

### `.github/`

Contains GitHub-specific project configuration.

In this project, the `skills` directory contains AI/agent-related instructions and reusable development skills used during the development workflow.

```text
.github/
└── skills/
```

### `.mvn/`

Contains configuration required by the Maven Wrapper.

The Maven Wrapper allows developers to build and run the project without installing Maven manually.

```text
.mvn/
└── wrapper/
```

It works together with:

```text
mvnw
mvnw.cmd
```

### `.specify/`

Contains configuration, templates, scripts, and supporting files used by Spec Kit.

This directory supports the Spec-Driven Development workflow and helps generate and manage specifications, plans, and development tasks.

### `specs/`

Contains the specifications of the application features.

For example:

```text
specs/
└── 001-simple-todo-app/
```

This directory can contain documents such as:

```text
spec.md
plan.md
tasks.md
research.md
data-model.md
```

These files describe the feature requirements and the implementation plan before or during development.

### `src/`

Contains the actual application source code.

```text
src/
├── main/
└── test/
```

#### `src/main/`

Contains the production code of the application.

Usually:

```text
src/main/
├── java/
└── resources/
```

`java/` contains application classes such as:

- Controllers
- Services
- Models
- Repositories
- Configuration classes

`resources/` contains application resources such as:

```text
application.properties
```

or:

```text
application.yml
```

### `src/test/`

Contains automated tests for the application.

Tests can include:

- Unit tests
- Service tests
- Controller tests
- Integration tests

### `.gitignore`

Defines files and directories that Git should not track.

For example:

```text
target/
.idea/
.vscode/
*.log
.env
```

Generated files, IDE-specific configuration, logs, and sensitive environment files should normally not be committed to the repository.

### `mvnw`

Maven Wrapper executable for Linux and macOS.

Example:

```bash
./mvnw spring-boot:run
```

### `mvnw.cmd`

Maven Wrapper executable for Windows.

Example:

```bash
mvnw.cmd spring-boot:run
```

### `pom.xml`

The main Maven configuration file.

It defines:

- Project information
- Java version
- Spring Boot version
- Dependencies
- Build plugins
- Test dependencies

For example, dependencies such as Spring Web are configured in this file.

### `README.md`

Contains documentation about the project.

It explains:

- What the project does
- Technologies used
- Project structure
- How to run the project
- How the Spec-Driven Development workflow is used

## Running the Application

### Windows

```bash
mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

## Build

```bash
./mvnw clean package
```

On Windows:

```bash
mvnw.cmd clean package
```

## API

Base path:

```text
/api/tasks
```

Example endpoints:

```text
POST   /api/tasks
GET    /api/tasks
PATCH  /api/tasks/{id}/done
DELETE /api/tasks/{id}
```

## Spec-Driven Development

This project was developed using a specification-driven approach.

The main development flow is:

```text
Requirement
    ↓
Specification
    ↓
Technical Plan
    ↓
Tasks
    ↓
Implementation
    ↓
Testing
```

The specifications and development artifacts are stored under:

```text
specs/
```

and the Spec Kit configuration is stored under:

```text
.specify/
```

## Author

Simple Todo project created as an example of developing a Spring Boot application using Spec-Driven Development.