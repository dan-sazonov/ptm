package com.ptm.view;

import com.ptm.model.Task;
import com.ptm.model.TaskManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TimelinePanel extends JPanel {
    private final TaskManager taskManager;
    private final JFrame parentFrame;
    private final Consumer<Task> deleteTaskAction;
    private final BiConsumer<Task, TaskForm> editTaskAction;
    private LocalDate currentStartDate;
    private int viewScale;

    public TimelinePanel(TaskManager taskManager, JFrame parentFrame, Consumer<Task> deleteTaskAction, BiConsumer<Task, TaskForm> editTaskAction) {
        this.taskManager = taskManager;
        this.parentFrame = parentFrame;
//        this.taskController = taskController;
        this.deleteTaskAction = deleteTaskAction;
        this.editTaskAction = editTaskAction;
        this.currentStartDate = LocalDate.now();
        this.viewScale = 7;
        setLayout(new BorderLayout());
        refreshView();
    }

    public void setViewScale(int days) {
        this.viewScale = days;
        refreshView();
    }

    public void moveToToday() {
        this.currentStartDate = LocalDate.now();
        setViewScale(1);
    }

    public void moveWeekForward() {
        this.currentStartDate = currentStartDate.plusDays(viewScale);
        refreshView();
    }

    public void moveWeekBackward() {
        this.currentStartDate = currentStartDate.minusDays(viewScale);
        refreshView();
    }


    public void refreshView() {
        removeAll();
        if (viewScale == 1) {
            DayTimelinePanel dayPanel = new DayTimelinePanel(taskManager.getTasksByDate(currentStartDate), currentStartDate);
            JScrollPane scrollPane = new JScrollPane(dayPanel);
            add(scrollPane, BorderLayout.CENTER);
        } else {
            JPanel headerPanel = createHeaderPanel();
            JPanel bodyPanel = createBodyPanel();
            add(headerPanel, BorderLayout.NORTH);
            add(bodyPanel, BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridLayout(1, viewScale));
        for (int i = 0; i < viewScale; i++) {
            LocalDate date = currentStartDate.plusDays(i);
            String dayLabel = date.format(java.time.format.DateTimeFormatter.ofPattern("EE, dd.MM"));
            int taskCount = (int) taskManager.countTasksByDate(date);

            JLabel label = new JLabel("<html>" + dayLabel + "<br>Задач: " + taskCount + "</html>", SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            headerPanel.add(label);
        }
        return headerPanel;
    }

    private JPanel createBodyPanel() {
        JPanel bodyPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        int maxTaskLayers = 0;

        for (int i = 0; i < viewScale; i++) {
            gbc.gridx = i;
            gbc.gridy = 0;
            gbc.weightx = 1.0; // Равномерное распределение столбцов
            gbc.weighty = 0;

            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.GRAY)); // Вертикальные полосы
            bodyPanel.add(dayPanel, gbc);
        }

        List<Task> tasks = taskManager.getTasks();
        for (Task task : tasks) {
            LocalDate taskStart = task.getStartDate();
            LocalDate taskEnd = task.getEndDate();

            if (!(taskEnd.isBefore(currentStartDate) || taskStart.isAfter(currentStartDate.plusDays(viewScale - 1)))) {
                int startIndex = Math.max(0, (int) (taskStart.toEpochDay() - currentStartDate.toEpochDay()));
                int endIndex = Math.min(viewScale - 1, (int) (taskEnd.toEpochDay() - currentStartDate.toEpochDay()));
                int taskWidth = endIndex - startIndex + 1;

                int taskLayer = findAvailableLayer(startIndex, endIndex, bodyPanel);
                maxTaskLayers = Math.max(maxTaskLayers, taskLayer + 1);

                gbc.gridx = startIndex;
                gbc.gridy = taskLayer;
                gbc.gridwidth = taskWidth;
                gbc.weightx = 0;
                gbc.weighty = 1.0;

                JButton taskButton = new JButton(task.getTitle());
                taskButton.setToolTipText(task.getDescription());
                taskButton.setBackground(new Color(200, 230, 255));
                taskButton.setOpaque(true);
                taskButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                taskButton.addActionListener(e -> openTaskEditForm(task));

                bodyPanel.add(taskButton, gbc);
            }
        }

        for (int i = 0; i < viewScale; i++) {
            for (int j = 0; j < maxTaskLayers; j++) {
                if (getComponentAtLayer(i, j, bodyPanel) == null) {
                    gbc.gridx = i;
                    gbc.gridy = j;
                    gbc.gridwidth = 1;
                    gbc.weightx = 1.0;
                    gbc.weighty = 1.0 / maxTaskLayers;

                    JPanel filler = new JPanel();
                    filler.setBorder(BorderFactory.createEmptyBorder());
                    bodyPanel.add(filler, gbc);
                }
            }
        }

        return bodyPanel;
    }

    private int findAvailableLayer(int startIndex, int endIndex, JPanel bodyPanel) {
        for (int layer = 0; layer < Integer.MAX_VALUE; layer++) {
            boolean available = true;
            for (int i = startIndex; i <= endIndex; i++) {
                if (getComponentAtLayer(i, layer, bodyPanel) != null) {
                    available = false;
                    break;
                }
            }
            if (available) return layer;
        }
        return 0;
    }

    private Component getComponentAtLayer(int col, int layer, JPanel bodyPanel) {
        for (Component comp : bodyPanel.getComponents()) {
            GridBagConstraints gbc = ((GridBagLayout) bodyPanel.getLayout()).getConstraints(comp);
            if (gbc.gridx == col && gbc.gridy == layer) {
                return comp;
            }
        }
        return null;
    }

    private void openTaskEditForm(Task task) {
        TaskForm taskForm = new TaskForm(parentFrame);

        taskForm.setTaskTitle(task.getTitle());
        taskForm.setTaskDescription(task.getDescription());
        taskForm.setTaskStartDate(task.getStartDate());
        taskForm.setTaskStartTime(task.getStartTime());
        taskForm.setTaskEndDate(task.getEndDate());
        taskForm.setTaskEndTime(task.getEndTime());

        taskForm.setVisible(true);

        if (taskForm.isDeleted()) {
            deleteTaskAction.accept(task);
            refreshView();
            return;
        }

        if (taskForm.isSaved()) {
            editTaskAction.accept(task, taskForm);
            refreshView();
        }
    }
}

