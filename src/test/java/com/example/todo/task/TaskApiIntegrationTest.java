package com.example.todo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskApiIntegrationTest {

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void createsAndListsTask() throws Exception {
        HttpResponse<String> created = post("{\"title\":\"تمرین Spec-Driven\"}");

        assertEquals(201, created.statusCode());
        assertTrue(created.body().matches("(?s).*\"id\"\\s*:\\s*\\d+.*"));
        assertTrue(created.body().contains("\"title\":\"تمرین Spec-Driven\""));
        assertTrue(created.body().contains("\"status\":\"TODO\""));

        HttpResponse<String> list = get("/api/tasks");
        assertEquals(200, list.statusCode());
        assertTrue(list.body().contains("تمرین Spec-Driven"));
    }

    @Test
    void listsAtLeastThreeValidStartupTasks() throws Exception {
        HttpResponse<String> list = get("/api/tasks");
        assertEquals(200, list.statusCode());
        String body = list.body();
        assertTrue(body.startsWith("["));
        String[] tasks = body.substring(1, body.length() - 1).split("(?<=\\}),");
        assertTrue(tasks.length >= 3);
        for (String task : tasks) {
            assertTrue(task.matches("(?s).*\"id\"\\s*:\\s*\\d+.*"));
            assertTrue(task.matches("(?s).*\"title\"\\s*:\\s*\"\\S.*"));
            assertTrue(task.matches("(?s).*\"status\"\\s*:\\s*\"(TODO|DONE)\".*"));
        }
    }

    @Test
    void rejectsBlankTitleWithPersianValidationError() throws Exception {
        HttpResponse<String> response = post("{\"title\":\"  \"}");

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("\"code\":\"VALIDATION_ERROR\""));
        assertTrue(containsPersian(response.body()));
    }

    @Test
    void rejectsMalformedJsonWithBadRequest() throws Exception {
        HttpResponse<String> response = post("{");

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("\"code\":\"BAD_REQUEST\""));
        assertTrue(containsPersian(response.body()));
    }

    @Test
    void completesTaskRepeatedlyAndReportsUnknownOrInvalidIds() throws Exception {
        HttpResponse<String> created = post("{\"title\":\"کار برای تکمیل\"}");
        assertEquals(201, created.statusCode());
        String id = created.body().replaceFirst("(?s).*\"id\"\\s*:\\s*(\\d+).*", "$1");

        HttpResponse<String> completed = put("/api/tasks/" + id + "/done");
        assertEquals(200, completed.statusCode());
        assertTrue(completed.body().contains("\"status\":\"DONE\""));

        HttpResponse<String> repeated = put("/api/tasks/" + id + "/done");
        assertEquals(200, repeated.statusCode());
        assertTrue(repeated.body().contains("\"status\":\"DONE\""));

        String tasksBeforeUnknownId = get("/api/tasks").body();
        HttpResponse<String> unknown = put("/api/tasks/9223372036854775807/done");
        assertEquals(404, unknown.statusCode());
        assertTrue(unknown.body().contains("\"code\":\"NOT_FOUND\""));
        assertTrue(containsPersian(unknown.body()));
        assertEquals(tasksBeforeUnknownId, get("/api/tasks").body());

        HttpResponse<String> invalid = put("/api/tasks/not-a-long/done");
        assertEquals(400, invalid.statusCode());
        assertTrue(invalid.body().contains("\"code\":\"BAD_REQUEST\""));
        assertTrue(containsPersian(invalid.body()));
    }

    private HttpResponse<String> get(String path) throws Exception {
        return send(HttpRequest.newBuilder(uri(path)).GET().build());
    }

    private HttpResponse<String> post(String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(uri("/api/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return send(request);
    }

    private HttpResponse<String> put(String path) throws Exception {
        return send(HttpRequest.newBuilder(uri(path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build());
    }

    private HttpResponse<String> send(HttpRequest request) throws Exception {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private URI uri(String path) {
        return URI.create("http://localhost:" + port + path);
    }

    private boolean containsPersian(String value) {
        return value.codePoints().anyMatch(character -> character >= 0x0600 && character <= 0x06FF);
    }
}
