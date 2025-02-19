package com.mathdev.task_management.api;

import java.util.Arrays;

public enum Priority {
    High("High"),
    Low("Low"),
    Normal("Normal");

    private String description;

    Priority(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Priority fromString(String value) {
        for (Priority priority : Priority.values()) {
            if (priority.getDescription().equalsIgnoreCase(value) || priority.name().equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Invalid priority value: " + value);
    }

}