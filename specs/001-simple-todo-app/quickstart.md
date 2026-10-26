# Quickstart: Validate Simple Todo REST API

This guide verifies the implemented Simple Todo REST API.

## Prerequisites

- Java 21 installed and available as `java`.
- Maven Wrapper files `mvnw` and `mvnw.cmd` are present in the repository.

## Run Automated Tests

From the repository root:

```powershell
.\mvnw.cmd test
```

Expected: the JUnit suite passes, including service rule tests and HTTP integration coverage for the API contract.

## Start the API

```powershell
.\mvnw.cmd spring-boot:run
```

Expected: the application starts on `http://localhost:8080` and seeds at least three in-memory tasks.

## Verify the Main Flows

List the startup tasks and confirm at least three records contain `id`, `title`, and `status`:

```powershell
$tasks = Invoke-RestMethod -Method Get -Uri 'http://localhost:8080/api/tasks'
$tasks | Format-Table id, title, status
```

Create a task and confirm the response has an assigned `id` and `status` `TODO`:

```powershell
$created = Invoke-RestMethod -Method Post -Uri 'http://localhost:8080/api/tasks' -ContentType 'application/json; charset=utf-8' -Body '{"title":"تمرین Spec-Driven"}'
$created
```

Complete it and repeat the request; both responses should show `DONE`:

```powershell
$taskId = $created.id
Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/tasks/$taskId/done"
Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/tasks/$taskId/done"
```

## Verify Error Outcomes

Post a title containing only spaces. Expect `400 Bad Request`, code `VALIDATION_ERROR`, and a Persian message:

```powershell
curl.exe -i -X POST 'http://localhost:8080/api/tasks' -H 'Content-Type: application/json' --data-binary '{"title":"   "}'
```

Send malformed JSON. Expect `400 Bad Request`, code `BAD_REQUEST`, and a Persian message:

```powershell
curl.exe -i -X POST 'http://localhost:8080/api/tasks' -H 'Content-Type: application/json' --data-binary '{'
```

Complete an id that is not a Long, such as `not-a-long`. Expect `400 Bad Request`, code `BAD_REQUEST`, and a Persian message:

```powershell
curl.exe -i -X PUT 'http://localhost:8080/api/tasks/not-a-long/done'
```

Complete a valid Long that does not exist, such as `9223372036854775807`. Expect `404 Not Found`, code `NOT_FOUND`, and a Persian message. List tasks again and confirm existing tasks are unchanged:

```powershell
curl.exe -i -X PUT 'http://localhost:8080/api/tasks/9223372036854775807/done'
Invoke-RestMethod -Method Get -Uri 'http://localhost:8080/api/tasks' | Format-Table id, title, status
```

See [REST API contract](contracts/rest-api.md) for the complete request/response shape and [data model](data-model.md) for task rules.
