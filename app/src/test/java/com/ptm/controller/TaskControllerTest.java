package com.ptm.controller;

import com.ptm.model.Task;
import com.ptm.model.TaskManager;
import com.ptm.utils.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    private TaskController taskController;
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManager();
        taskController = new TaskController(taskManager, "test_tasks.xml");
    }

    @Test
    void testCreateTask() {
        Task task = taskController.createTask(
                "Test Task",
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalDate.now().plusDays(1),
                LocalTime.of(17, 0),
                "Description"
        );

        assertNotNull(task);
        assertTrue(taskManager.getTasks().contains(task));
    }

    @Test
    void testEditTask() {
        Task task = taskController.createTask(
                "Test Task",
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalDate.now().plusDays(1),
                LocalTime.of(17, 0),
                "Description"
        );

        taskController.editTask(
                task,
                "Updated Task",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                "Updated Description"
        );

        assertEquals("Updated Task", task.getTitle());
        assertEquals(LocalDate.now().plusDays(1), task.getStartDate());
        assertEquals("Updated Description", task.getDescription());
    }

    @Test
    void testDeleteTask() {
        Task task = taskController.createTask(
                "Test Task",
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalDate.now().plusDays(1),
                LocalTime.of(17, 0),
                "Description"
        );

        taskController.deleteTask(task);
        assertTrue(taskManager.getTasks().contains(task), "Task was not removed from TaskManager");
        TaskManager loadedManager = new XMLParser("test_tasks.xml").loadTasks();
        assertTrue(loadedManager.getTasks().contains(task), "Task was not removed from XML");
    }

}
