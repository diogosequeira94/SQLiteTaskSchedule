package com.example.sqltasksnew.Model;

import java.io.Serializable;

//Serializable To send Data to the other activity

public class Task implements Serializable {

    private Long id;
    private String taskName;
    private int image;
    private String notes;
    private String deadline;

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


    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
