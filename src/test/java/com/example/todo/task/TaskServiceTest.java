package com.example.todo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(new InMemoryTaskRepository());
    }

    @Test
    void createsTasksWithDistinctIdsAndTodoStatus() {
        Task first = taskService.createTask("خرید نان");
        Task second = taskService.createTask("مطالعه");

        assertNotEquals(first.id(), second.id());
        assertEquals("خرید نان", first.title());
        assertEquals(TaskStatus.TODO, first.status());
    }

    @Test
    void rejectsMissingOrBlankTitles() {
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(null));
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(""));
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(" \t "));
    }

    @Test
    void completingATaskIsIdempotentAndUnknownIdsDoNotChangeStoredTasks() {
        Task task = taskService.createTask("تمرین");
        var before = taskService.listTasks();

        Task completed = taskService.completeTask(task.id());
        Task repeated = taskService.completeTask(task.id());
        var afterCompletion = taskService.listTasks();

        assertEquals(TaskStatus.DONE, completed.status());
        assertEquals(completed, repeated);
        assertThrows(java.util.NoSuchElementException.class,
                () -> taskService.completeTask(Long.MAX_VALUE));
        assertEquals(before.size(), afterCompletion.size());
        assertEquals(afterCompletion, taskService.listTasks());
        assertTrue(taskService.listTasks().contains(completed));
    }
}
