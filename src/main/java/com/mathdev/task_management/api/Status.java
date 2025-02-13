package com.mathdev.task_management.api;

public enum Status{
    DONE("Done"),
    READY("Ready"),
    PROGRESS("Progress");

    private String description;

    Status (String descreption) {
        this.description = descreption;
    }

    public String getDescreption() {
        return description;
    }

    public void setDescreption(String descreption) {
        this.description = descreption;
    }
}
