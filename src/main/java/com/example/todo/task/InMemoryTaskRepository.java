package com.example.todo.task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryTaskRepository implements TaskRepository {

    private final AtomicLong nextId = new AtomicLong(1);
    private final Map<Long, Task> tasks = new LinkedHashMap<>();

    public InMemoryTaskRepository() {
        save(new Task(null, "خرید نان", TaskStatus.TODO));
        save(new Task(null, "مطالعه", TaskStatus.TODO));
        save(new Task(null, "پیاده‌روی", TaskStatus.TODO));
    }

    @Override
    public Task save(Task task) {
        Task storedTask = task.id() == null
                ? new Task(nextId.getAndIncrement(), task.title(), task.status())
                : task;
        tasks.put(storedTask.id(), storedTask);
        return storedTask;
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }
}
