package com.ptm.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskCreation() {
        Task task = new Task(
                "Test Task",
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalDate.now().plusDays(1),
                LocalTime.of(17, 0),
                "Description"
        );

        assertEquals("Test Task", task.getTitle());
        assertEquals(LocalDate.now(), task.getStartDate());
        assertEquals(LocalTime.of(9, 0), task.getStartTime());
        assertEquals("Description", task.getDescription());
    }

    @Test
    void testInvalidDates() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Task(
                    "Invalid Task",
                    LocalDate.now().plusDays(1),
                    LocalTime.of(9, 0),
                    LocalDate.now(),
                    LocalTime.of(17, 0),
                    "Invalid dates"
            );
        });
    }
}
