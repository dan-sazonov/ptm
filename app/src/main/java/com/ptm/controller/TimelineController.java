package com.ptm.controller;

import com.ptm.model.TaskManager;

import java.time.LocalDate;

public class TimelineController {
    private final TaskManager taskManager;
    private LocalDate currentViewStartDate;
    private int viewScale;

    public TimelineController(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.currentViewStartDate = LocalDate.now();
        this.viewScale = 7;
    }

    // Установить масштаб
    public void setViewScale(int days) {
        if (days == 3 || days == 7) {
            this.viewScale = days;
        } else {
            throw new IllegalArgumentException("The scale should be 3 or 7 days");
        }
    }

    // Получить текущий масштаб
    public int getViewScale() {
        return viewScale;
    }

    // Переход на один шаг назад
    public void moveBack() {
        currentViewStartDate = currentViewStartDate.minusDays(viewScale);
    }

    // Переход на один шаг вперед
    public void moveForward() {
        currentViewStartDate = currentViewStartDate.plusDays(viewScale);
    }

    // Переход к сегодняшнему дню
    public void moveToToday() {
        currentViewStartDate = LocalDate.now();
    }

    // Получить дату начала текущего вида
    public LocalDate getCurrentViewStartDate() {
        return currentViewStartDate;
    }
}
