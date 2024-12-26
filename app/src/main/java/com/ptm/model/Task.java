package com.ptm.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Task {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;

    public Task(String title, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String description) {
        if (startDate.isAfter(endDate) || (startDate.equals(endDate) && startTime.isAfter(endTime))) {
            throw new IllegalArgumentException("Время начала не может быть позже времени завершения.");
        }
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) &&
                Objects.equals(startDate, task.startDate) &&
                Objects.equals(startTime, task.startTime) &&
                Objects.equals(endDate, task.endDate) &&
                Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, startDate, startTime, endDate, endTime);
    }
}
