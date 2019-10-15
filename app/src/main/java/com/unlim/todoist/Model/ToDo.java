package com.unlim.todoist.Model;

import com.google.gson.annotations.SerializedName;
import com.unlim.todoist.Presenter.Const;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class ToDo implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("deadline")
    private Date deadline;
    @SerializedName("priority")
    private int priority;

    public ToDo(String name, String description, Date deadline, int priority) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public String getDeadlineStr() {
        DateFormat dateFormat = new SimpleDateFormat(Const.DATE_FORMAT);
        return dateFormat.format(deadline);
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        String priorityText = "H";
        if (priority <= 0) {
            priorityText = "L";
        }
        return priorityText;
    }

    public String getPriorityFullStr() {
        String priorityText = "HIGH";
        if (priority <= 0) {
            priorityText = "LOW";
        }
        return priorityText;
    }

    public int getIntPriority() {
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
        ToDo toDo = (ToDo) o;
        return id == toDo.id &&
                priority == toDo.priority &&
                name.equals(toDo.name) &&
                Objects.equals(description, toDo.description) &&
                deadline.equals(toDo.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, deadline, priority);
    }

    public boolean isExpired() {
        return deadline.before(new Date());
    }
}
