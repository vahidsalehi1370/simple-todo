package com.example.todo.task;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("عنوان کار نمی‌تواند خالی باشد.");
        }

        return taskRepository.save(new Task(null, title, TaskStatus.TODO));
    }

    public List<Task> listTasks() {
        return taskRepository.findAll();
    }

    public Task completeTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("کار موردنظر پیدا نشد."));
        if (task.status() == TaskStatus.DONE) {
            return task;
        }
        return taskRepository.save(new Task(task.id(), task.title(), TaskStatus.DONE));
    }
}
