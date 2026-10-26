# Tasks: Simple Todo REST API

**Input**: Design documents from `/specs/001-simple-todo-app/`

**Prerequisites**: `plan.md`, `spec.md`, `research.md`, `data-model.md`, `contracts/rest-api.md`, `quickstart.md`

**Tests**: JUnit tasks are included because the user explicitly requested JUnit. Write story tests before the corresponding implementation and confirm they fail for the expected missing behavior.

**Organization**: Tasks are grouped by user story for incremental implementation and validation. The supplied T001-T011 work items are represented below, with story-specific test and endpoint work made explicit.

## Format

Every task uses `- [ ] T###`, an optional `[P]` marker only for independent work, an optional user-story label for story phases, and an exact repository-relative file path.

## Path Conventions

This is a single Maven project. Java production files live under `src/main/java/com/example/todo/`; tests live under `src/test/java/com/example/todo/task/`.

## Phase 1: Setup

**Purpose**: Initialize the Java 21 Spring Boot project using only the planned runtime and test dependencies.

- [X] T001 Create the Maven project in `pom.xml`, add the Maven Wrapper files `mvnw` and `mvnw.cmd`, and add the application entry point at `src/main/java/com/example/todo/TodoApplication.java`; configure Java 21 with `spring-boot-starter-webmvc` and test-scope `spring-boot-starter-test` only.

## Phase 2: Foundational

**Purpose**: Create shared task types, in-memory storage boundaries, and API error mapping required by all stories.

- [X] T002 [P] Create the `TODO` and `DONE` enum values in `src/main/java/com/example/todo/task/TaskStatus.java`.
- [X] T003 Create `Task` in `src/main/java/com/example/todo/task/Task.java` with a `Long id`, required `String title`, and `TaskStatus status`; keep id assignment system-owned and avoid persistence annotations.
- [X] T004 Define `save`, `findAll`, and `findById` operations in `src/main/java/com/example/todo/task/TaskRepository.java` using the `Task` model and `Long` identifiers.
- [X] T005 Implement `TaskRepository` with a simple in-memory collection and system-generated Long ids unique for the lifetime of the process in `src/main/java/com/example/todo/task/InMemoryTaskRepository.java`; do not add startup sample data in this task.
- [X] T006 [P] Add the Persian `ApiError` response model in `src/main/java/com/example/todo/task/ApiError.java` and centralized mappings in `src/main/java/com/example/todo/task/ApiExceptionHandler.java`: malformed JSON and invalid Long path syntax map to HTTP 400 `BAD_REQUEST`, blank-title validation maps to HTTP 400 `VALIDATION_ERROR`, and a valid unknown task id maps to HTTP 404 `NOT_FOUND`.

## Phase 3: User Story 1 - Create and List Tasks (Priority: P1)

**Goal**: A user can create a task with a nonblank title and see the created task in the collection.

**Independent Test**: Submit a valid title, receive a system-generated Long id and `TODO` status, then retrieve the collection and find the same task. A blank or whitespace-only title is rejected with a Persian validation message.

### Tests for User Story 1

- [X] T007 [P] [US1] Add JUnit service tests that create at least two tasks and assert their generated `Long` ids are distinct, verify default `TODO`, and reject null, empty, or whitespace-only titles in `src/test/java/com/example/todo/task/TaskServiceTest.java`.
- [X] T008 [P] [US1] Add REST integration tests for successful task creation, response fields, visibility of the created task in `GET /api/tasks`, and Persian error messages for blank titles and malformed JSON returning `BAD_REQUEST` in `src/test/java/com/example/todo/task/TaskApiIntegrationTest.java`.

### Implementation for User Story 1

- [X] T009 [US1] Implement create and list operations in `src/main/java/com/example/todo/task/TaskService.java`; reject titles for which `String.isBlank()` is true, retain other nonblank titles as submitted, initialize new tasks as `TODO`, and delegate system-generated Long ids to the repository.
- [X] T010 [P] [US1] Add `CreateTaskRequest` in `src/main/java/com/example/todo/task/CreateTaskRequest.java` with only the client-supplied title field; do not accept client-supplied id or status.
- [X] T011 [US1] Add `POST /api/tasks` and `GET /api/tasks` handlers returning the contract response shapes in `src/main/java/com/example/todo/task/TaskController.java`.

**Checkpoint**: Creation and listing of newly created tasks are independently usable; invalid titles produce the documented Persian error.

## Phase 4: User Story 2 - View Startup Sample Tasks (Priority: P2)

**Goal**: A user can view at least three sample tasks present whenever the application starts.

**Independent Test**: Start a fresh application process and request the collection; at least three records each contain `id`, `title`, and `status`. The order is not asserted.

### Tests for User Story 2

- [X] T012 [US2] Extend `src/test/java/com/example/todo/task/TaskApiIntegrationTest.java` to assert at least three startup sample tasks with nonblank titles, Long ids, and only `TODO` or `DONE` statuses, without asserting ordering or exact titles.

### Implementation for User Story 2

- [X] T013 [US2] Seed at least three nonblank sample tasks with unique system-generated Long ids when the in-memory repository is initialized in `src/main/java/com/example/todo/task/InMemoryTaskRepository.java`.

**Checkpoint**: A fresh application start exposes at least three sample tasks through the existing list endpoint.

## Phase 5: User Story 3 - Complete Tasks (Priority: P3)

**Goal**: A user can mark an existing task done; repeating the operation is successful, while unknown or malformed ids return their specified errors.

**Independent Test**: Complete an existing task and observe `DONE`; repeat and observe the same successful state; request a valid but unknown Long and observe `NOT_FOUND`; request an id that is not syntactically a Long and observe `BAD_REQUEST`.

### Tests for User Story 3

- [X] T014 [P] [US3] Add JUnit service tests for the `TODO` to `DONE` transition, idempotent completion, and completing a valid unknown Long id; assert that the unknown-id operation leaves every existing task unchanged in `src/test/java/com/example/todo/task/TaskServiceTest.java`.
- [X] T015 [P] [US3] Add REST integration tests for first and repeated completion; verify a Persian error message for `404 NOT_FOUND` on an unknown Long without changing existing tasks, and for `400 BAD_REQUEST` on invalid id syntax in `src/test/java/com/example/todo/task/TaskApiIntegrationTest.java`.

### Implementation for User Story 3

- [X] T016 [US3] Implement task completion in `src/main/java/com/example/todo/task/TaskService.java`; change `TODO` to `DONE`, leave an already `DONE` task unchanged without error, and report a missing task as `NOT_FOUND`.
- [X] T017 [US3] Add `PUT /api/tasks/{id}/done` in `src/main/java/com/example/todo/task/TaskController.java`, binding the path id as `Long` and returning the completed task response.

**Checkpoint**: All three user journeys and the specified malformed, invalid, repeated, and missing-resource outcomes are covered.

## Phase 6: Polish and Cross-Cutting Concerns

**Purpose**: Keep the runnable guide aligned with the final contract and validate the complete feature.

- [X] T018 Update `specs/001-simple-todo-app/quickstart.md` with runnable examples and expected results for malformed JSON (`BAD_REQUEST`), invalid Long id syntax (`BAD_REQUEST`), and valid unknown ids (`NOT_FOUND`).
- [X] T019 After T018, run `mvnw.cmd test` from the repository root and record the final test and manual quickstart commands and expected outcomes in `specs/001-simple-todo-app/quickstart.md`.

---

## Dependencies and Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies; must finish before all Java tasks.
- **Foundational (Phase 2)**: Depends on T001; blocks every user story. T002 precedes T003, T003 precedes T004, and T004 precedes T005. T006 can run independently after T001.
- **User Story 1 (Phase 3)**: Depends on the foundational types, repository, and error mapping. Tests T007-T008 precede their implementation tasks.
- **User Story 2 (Phase 4)**: Depends on User Story 1's collection endpoint; T012 precedes startup seeding T013.
- **User Story 3 (Phase 5)**: Depends on the shared service and controller from User Story 1 and the startup task data from User Story 2. Tests T014-T015 precede implementation T016-T017.
- **Polish (Phase 6)**: Depends on all three user stories.

Quickstart task order:

```text
T018
	↓
T019
```

### User Story Dependencies

**Intentional overlap**: US1 introduces the list endpoint so its creation flow can be independently verified; US2 reuses that same endpoint specifically to validate startup sample tasks.

- **US1 (P1)**: Starts after Phase 2. Includes the minimum collection endpoint needed to show a newly created task.
- **US2 (P2)**: Follows US1 because it adds startup sample data to the existing list behavior.
- **US3 (P3)**: Follows US2 in the planned sequence so completion can be demonstrated against seeded tasks; it also works with a task created through US1.

### Parallel Opportunities

- T002 and T006 can be done in parallel after T001 because they modify different files and do not depend on each other.
- T007 and T008 can be authored in parallel because they are separate test files; both precede US1 implementation.
- T009 and T010 can be implemented in parallel because service logic and the request record are separate files; T011 depends on both.
- T012 can be authored before T013; the test and repository implementation touch different files.
- T014 and T015 can be authored in parallel in separate test files; both precede US3 implementation.

## Parallel Example: User Story 1

After Phase 2, prepare T007 and T008 at the same time in their separate test files. Once both tests are in place, implement T009 and T010 in parallel, then complete T011 after both are ready.

## Implementation Strategy

### MVP First

1. Complete Phase 1 Setup and Phase 2 Foundational tasks.
2. Complete Phase 3 User Story 1.
3. Run the focused service and REST tests for creation and collection visibility.
4. Stop for review; defer startup sample seeding and completion behavior to the next increments.

### Incremental Delivery

1. Setup and foundation, then create/list newly created tasks (US1).
2. Add startup sample tasks and verify the list on a fresh process (US2).
3. Add task completion, idempotency, and malformed/unknown id behavior (US3).
4. Run the complete test suite and validate the quickstart guide.

### Test Strategy

Write the JUnit tests for each story before implementing that story's service/controller behavior. Use service tests for application rules and REST integration tests for HTTP status codes, response fields, and Persian error messages. No database, authentication, frontend, concurrency guarantee, performance SLA, or API versioning task is in scope.

## Phase 7: Convergence

- [X] T020 Add JUnit service and REST integration tests for creation, listing, title validation, startup samples, completion and idempotency, unknown ids, malformed JSON, and invalid Long syntax per Constitution III (missing).
- [X] T021 Implement the application service rules and create/list REST API, including system-assigned ids, TODO status, title validation, and the request/response models per FR-001, FR-002, FR-003, FR-004, FR-010, and Constitution IV (missing).
- [X] T022 Seed at least three nonblank sample tasks with system-generated ids when the in-memory repository initializes per FR-005 and US2 (missing).
- [X] T023 Implement the completion service rule and `PUT /api/tasks/{id}/done`, preserving DONE on repeated requests and reporting unknown task ids without changing other tasks per FR-007, FR-008, FR-009, and US3 (missing).
- [X] T024 Add centralized Persian API error responses for blank titles, malformed JSON, invalid Long path syntax, and valid unknown ids with the specified HTTP statuses and error codes per FR-010, FR-013, and plan: error mapping (missing).
- [X] T025 Correct the quickstart's stale claim that application source and Maven Wrapper are absent, and add runnable malformed-JSON and invalid-Long examples with expected outcomes per T018-T019 (partial).
