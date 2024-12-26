package com.ptm.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private final List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getTasksByDate(LocalDate date) {
        return tasks.stream()
                .filter(task -> !task.getEndDate().isBefore(date) && !task.getStartDate().isAfter(date))
                .collect(Collectors.toList());
    }

    public long countTasksByDate(LocalDate date) {
        return getTasksByDate(date).size();
    }
}
