package com.unlim.todoist.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ToDoModel implements Serializable {

    private String name;
    private String description;
    private Date deadline;
    private int priority;

    public ToDoModel(String name, String description, Date deadline, int priority) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDoModel toDoModel = (ToDoModel) o;
        return priority == toDoModel.priority &&
                name.equals(toDoModel.name) &&
                Objects.equals(description, toDoModel.description) &&
                deadline.equals(toDoModel.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, deadline, priority);
    }

}

