package com.ptm.view;

import com.ptm.model.Task;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DayTimelinePanel extends JPanel {
    private final List<Task> tasks;
    private final LocalDate currentDate;

    public DayTimelinePanel(List<Task> tasks, LocalDate currentDate) {
        this.tasks = tasks;
        this.currentDate = currentDate;
        setLayout(null);
        setPreferredSize(new Dimension(700, 1200));
        refreshView();
    }

    public void refreshView() {
        removeAll();

        for (int hour = 0; hour < 24; hour++) {
            JLabel hourLabel = new JLabel(hour + ":00", SwingConstants.RIGHT);
            hourLabel.setBounds(0, hour * 50, 50, 50);
            hourLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            add(hourLabel);
        }

        List<List<Task>> timeSlots = new ArrayList<>();
        for (int i = 0; i < 24 * 60; i++) {
            timeSlots.add(new ArrayList<>());
        }

        for (Task task : tasks) {
            if (taskIntersectsWithCurrentDay(task)) {
                JButton taskButton = createTaskButton(task);
                positionTaskButton(taskButton, task, timeSlots);
                add(taskButton);
            }
        }

        revalidate();
        repaint();
    }

    private boolean taskIntersectsWithCurrentDay(Task task) {
        LocalDate taskStartDate = task.getStartDate();
        LocalDate taskEndDate = task.getEndDate();

        return !taskStartDate.isAfter(currentDate) && !taskEndDate.isBefore(currentDate);
    }

    private JButton createTaskButton(Task task) {
        JButton button = new JButton(task.getTitle());
        button.setBackground(new Color(200, 230, 255));
        button.setToolTipText(task.getDescription());
        return button;
    }

    private void positionTaskButton(JButton button, Task task, List<List<Task>> timeSlots) {
        LocalTime startTime = task.getStartDate().isBefore(currentDate) ? LocalTime.MIN : task.getStartTime();
        LocalTime endTime = task.getEndDate().isAfter(currentDate) ? LocalTime.MAX : task.getEndTime();

        int startY = (int) ((startTime.getHour() + startTime.getMinute() / 60.0) * 50);
        int endY = (int) ((endTime.getHour() + endTime.getMinute() / 60.0) * 50);

        int startMinute = startTime.getHour() * 60 + startTime.getMinute();
        int endMinute = endTime.getHour() * 60 + endTime.getMinute();

        int row = findAvailableRow(startMinute, endMinute, timeSlots);

        for (int minute = startMinute; minute < endMinute; minute++) {
            timeSlots.get(minute).add(task);
        }

        int buttonX = 60 + row * 220;
        button.setBounds(buttonX, startY, 200, endY - startY);
    }

    private int findAvailableRow(int startMinute, int endMinute, List<List<Task>> timeSlots) {
        for (int row = 0; row < Integer.MAX_VALUE; row++) {
            boolean rowAvailable = true;
            for (int minute = startMinute; minute < endMinute; minute++) {
                if (row < timeSlots.get(minute).size()) {
                    rowAvailable = false;
                    break;
                }
            }
            if (rowAvailable) {
                return row;
            }
        }
        return 0;
    }
}
