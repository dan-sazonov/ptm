package com.ptm.controller;

import com.ptm.model.Task;
import com.ptm.model.TaskManager;
import com.ptm.utils.XMLParser;

import java.time.LocalDate;
import java.time.LocalTime;

public class TaskController {
    private final TaskManager taskManager;
    private final XMLParser xmlParser;

    public TaskController(TaskManager taskManager, String filePath) {
        this.taskManager = taskManager;
        this.xmlParser = new XMLParser(filePath);

        TaskManager loadedManager = xmlParser.loadTasks();
        for (Task task : loadedManager.getTasks()) {
            taskManager.addTask(task);
        }
    }

    public Task createTask(String title, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String description) {
        Task newTask = new Task(title, startDate, startTime, endDate, endTime, description);
        taskManager.addTask(newTask);
        xmlParser.saveTasks(taskManager);
        return newTask;
    }
    public void editTask(Task task, String newTitle, LocalDate newStartDate, LocalDate newEndDate, String newDescription) {
        task.setTitle(newTitle);
        task.setStartDate(newStartDate);
        task.setEndDate(newEndDate);
        task.setDescription(newDescription);
        xmlParser.saveTasks(taskManager);
    }

    public void deleteTask(Task task) {
        taskManager.removeTask(task);
        xmlParser.saveTasks(taskManager);
    }
}
