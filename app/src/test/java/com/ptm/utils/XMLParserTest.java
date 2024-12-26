package com.ptm.utils;

import com.ptm.model.Task;
import com.ptm.model.TaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class XMLParserTest {

    @Test
    void testSaveAndLoadTasks() {
        TaskManager taskManager = new TaskManager();
        Task task = new Task(
                "Test Task",
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalDate.now().plusDays(1),
                LocalTime.of(17, 0),
                "Description"
        );

        taskManager.addTask(task);

        String testFilePath = "test_tasks.xml";
        XMLParser parser = new XMLParser(testFilePath);
        parser.saveTasks(taskManager);

        TaskManager loadedManager = parser.loadTasks();
        assertEquals(1, loadedManager.getTasks().size());
        assertEquals(task.getTitle(), loadedManager.getTasks().get(0).getTitle());

        new File(testFilePath).delete();
    }
}
