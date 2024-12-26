package com.ptm.view;

import com.ptm.controller.TaskController;
import com.ptm.model.TaskManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final TimelinePanel timelinePanel;
    private final TaskController taskController;

    public MainFrame() {
        setTitle("Personal Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        TaskManager taskManager = new TaskManager();
        taskController = new TaskController(taskManager, "tasks.xml");

        timelinePanel = new TimelinePanel(
                taskManager,
                this,
                taskController::deleteTask,
                (task, taskForm) -> taskController.editTask(
                        task,
                        taskForm.getTaskTitle(),
                        taskForm.getTaskStartDate(),
                        taskForm.getTaskEndDate(),
                        taskForm.getTaskDescription()
                )
        );
        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        add(timelinePanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel menuPanel = new JPanel();
        JButton createTaskButton = new JButton("Создать задачу");
        createTaskButton.addActionListener(e -> openTaskForm());
        JButton todayButton = new JButton("Сегодня");
        todayButton.addActionListener(e -> timelinePanel.moveToToday());

        JComboBox<String> scaleSelector = new JComboBox<>(new String[]{"3 дня", "Неделя", "Сегодня"});
        scaleSelector.addActionListener(e -> {
            String selected = (String) scaleSelector.getSelectedItem();
            if ("3 дня".equals(selected)) timelinePanel.setViewScale(3);
            else if ("Неделя".equals(selected)) timelinePanel.setViewScale(7);
            else if ("Сегодня".equals(selected)) timelinePanel.moveToToday();
        });

        menuPanel.add(createTaskButton);
        menuPanel.add(todayButton);
        menuPanel.add(scaleSelector);

        // Кнопки переключения недель
        JPanel navigationPanel = new JPanel();
        JButton prevWeekButton = new JButton("< Раньше");
        prevWeekButton.addActionListener(e -> timelinePanel.moveWeekBackward());
        JButton nextWeekButton = new JButton("Позже >");
        nextWeekButton.addActionListener(e -> timelinePanel.moveWeekForward());
        navigationPanel.add(prevWeekButton);
        navigationPanel.add(nextWeekButton);

        topPanel.add(menuPanel, BorderLayout.WEST);
        topPanel.add(navigationPanel, BorderLayout.EAST);
        return topPanel;
    }

    private void openTaskForm() {
        TaskForm taskForm = new TaskForm(this);
        taskForm.setVisible(true);
        if (taskForm.isSaved()) {
            taskController.createTask(
                    taskForm.getTaskTitle(),
                    taskForm.getTaskStartDate(),
                    taskForm.getTaskStartTime(),
                    taskForm.getTaskEndDate(),
                    taskForm.getTaskEndTime(),
                    taskForm.getTaskDescription()
            );
            timelinePanel.refreshView();
        }
    }
}
