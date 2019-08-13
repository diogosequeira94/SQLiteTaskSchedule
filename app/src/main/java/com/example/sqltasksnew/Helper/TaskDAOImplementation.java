package com.example.sqltasksnew.Helper;

import com.example.sqltasksnew.Model.Task;

import java.util.List;

public interface TaskDAOImplementation {


    public boolean save(Task task);

    public boolean update(Task task);

    public boolean delete(Task task);


    public List<Task> list();
}
