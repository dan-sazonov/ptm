package com.ptm.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    @Test
    void testAddTask() {
        TaskManager taskManager = new TaskManager();
        Task task = new Task(
                "Task 1",
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalDate.now().plusDays(2),
                LocalTime.of(17, 0),
                "Description"
        );

        taskManager.addTask(task);
        assertTrue(taskManager.getTasks().contains(task));
    }

    @Test
    void testRemoveTask() {
        TaskManager taskManager = new TaskManager();
        Task task = new Task(
                "Task 1",
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalDate.now().plusDays(2),
                LocalTime.of(17, 0),
                "Description"
        );

        taskManager.addTask(task);
        taskManager.removeTask(task);
        assertFalse(taskManager.getTasks().contains(task));
    }

    @Test
    void testGetTasksByDate() {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task(
                "Task 1",
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalDate.now().plusDays(2),
                LocalTime.of(17, 0),
                "Description"
        );

        Task task2 = new Task(
                "Task 2",
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalDate.now(),
                LocalTime.of(17, 0),
                "Another Description"
        );

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        assertEquals(2, taskManager.getTasksByDate(LocalDate.now()).size());
    }
}
