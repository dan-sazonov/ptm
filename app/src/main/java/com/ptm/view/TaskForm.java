package com.ptm.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class TaskForm extends JDialog {
    private final JTextField titleField;
    private final JTextArea descriptionArea;
    private final JSpinner startDateSpinner;
    private final JSpinner startTimeSpinner;
    private final JSpinner endDateSpinner;
    private final JSpinner endTimeSpinner;
    private boolean isSaved;
    private boolean isDeleted;

    public TaskForm(Frame owner) {
        super(owner, "Создать/Редактировать задачу", true);
        setSize(400, 400);
        setLocationRelativeTo(owner);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel("Название:"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField(20);
        add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Описание:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea();
        descriptionArea.setRows(5);
        descriptionArea.setColumns(20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel("Дата начала:"), gbc);

        gbc.gridx = 1;
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "dd.MM.yyyy"));
        add(startDateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Время начала:"), gbc);

        gbc.gridx = 1;
        startTimeSpinner = new JSpinner(new SpinnerDateModel());
        startTimeSpinner.setEditor(new JSpinner.DateEditor(startTimeSpinner, "HH:mm"));
        add(startTimeSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Дата окончания:"), gbc);

        gbc.gridx = 1;
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "dd.MM.yyyy"));
        add(endDateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Время окончания:"), gbc);

        gbc.gridx = 1;
        endTimeSpinner = new JSpinner(new SpinnerDateModel());
        endTimeSpinner.setEditor(new JSpinner.DateEditor(endTimeSpinner, "HH:mm"));
        add(endTimeSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> {
            isSaved = true;
            dispose();
        });
        add(saveButton, gbc);

        gbc.gridx = 1;
        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(e -> {
            isDeleted = true;
            dispose();
        });
        add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton, gbc);
    }

    public boolean isSaved() {
        return isSaved;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getTaskTitle() {
        return titleField.getText();
    }

    public String getTaskDescription() {
        return descriptionArea.getText();
    }

    public LocalDate getTaskStartDate() {
        return ((Date) startDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalTime getTaskStartTime() {
        return ((Date) startTimeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    public LocalDate getTaskEndDate() {
        return ((Date) endDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalTime getTaskEndTime() {
        return ((Date) endTimeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    public void setTaskTitle(String title) {
        titleField.setText(title);
    }

    public void setTaskDescription(String description) {
        descriptionArea.setText(description);
    }

    public void setTaskStartDate(LocalDate startDate) {
        Date date = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        startDateSpinner.setValue(date);
    }

    public void setTaskStartTime(LocalTime startTime) {
        Date date = Date.from(startTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
        startTimeSpinner.setValue(date);
    }

    public void setTaskEndDate(LocalDate endDate) {
        Date date = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        endDateSpinner.setValue(date);
    }

    public void setTaskEndTime(LocalTime endTime) {
        Date date = Date.from(endTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
        endTimeSpinner.setValue(date);
    }
}
