package com.mathdev.task_management.api;

public enum Priority {
    HIGH("High"),
    NORMAL("Normal"),
    LOW("Low");

    private String description;

    Priority (String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descreption) {
        this.description = descreption;
    }
}
