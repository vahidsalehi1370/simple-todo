# REST API Contract: Simple Todo

Base path: `/api/tasks`

Requests and successful responses use JSON. User-facing messages are in Persian. The `status` field is always `TODO` or `DONE`; error codes are not task statuses. Task `id` is a system-generated JSON integer representing a Java `Long`, unique for the lifetime of the running application.

The API has no version prefix in this initial release. Task ordering in list responses is not guaranteed. Concurrent request behavior is outside scope. No performance, availability, or capacity SLA is required.

## Create Task

`POST /api/tasks`

Request:

```json
{
  "title": "خرید نان"
}
```

Success: `201 Created`

```json
{
  "id": 4,
  "title": "خرید نان",
  "status": "TODO"
}
```

The client supplies only `title`. The service rejects a missing, empty, or whitespace-only title with HTTP `400 Bad Request` and an `ApiError` body whose `code` is `VALIDATION_ERROR` and whose `message` is Persian. Malformed JSON returns HTTP `400 Bad Request` with code `BAD_REQUEST`.

## List Tasks

`GET /api/tasks`

Success: `200 OK`, with a JSON array of task objects. At application startup, at least three sample tasks are present. Each object contains `id` (a system-generated Long), `title`, and `status`. The response order is unspecified.

## Complete Task

`PUT /api/tasks/{id}/done`

Success: `200 OK`, returning the task with status `DONE`. Calling this endpoint again for the same completed task also returns `200 OK` with status `DONE`.

If `{id}` is not syntactically a Long, return HTTP `400 Bad Request` with an `ApiError` body whose `code` is `BAD_REQUEST` and whose `message` is Persian. If it is a syntactically valid Long that does not identify a task, return HTTP `404 Not Found` with code `NOT_FOUND` and a Persian message. The operation does not modify any other task.

## Error Shape

```json
{
  "code": "NOT_FOUND",
  "message": "Task موردنظر پیدا نشد."
}
```

Malformed JSON and invalid task-id syntax use `BAD_REQUEST`; blank or missing titles use `VALIDATION_ERROR`; a valid but unknown task id uses `NOT_FOUND`. No authentication headers, database-backed behavior, concurrent-request guarantee, or API versioning are required.
