package com.example.sqltasksnew.Model;

import java.io.Serializable;

//Serializable To send Data to the other activity

public class Task implements Serializable {

    private Long id;
    private String taskName;
    private boolean important;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }
}
