package com.example.sqltasksnew.Model;

import java.io.Serializable;

//Serializable To send Data to the other activity

public class Task implements Serializable {

    private Long id;
    private String taskName;
    private boolean important;
    private int image;

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

    public int importantValue(){

        if(!important){

            image = 0;

        } else {

            image = 1;
        }

        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }
}
