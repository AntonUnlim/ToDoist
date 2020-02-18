package com.unlim.todoist.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.unlim.todoist.Presenter.Const;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.arch.persistence.room.ColumnInfo.INTEGER;

@Entity
public class ToDo implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    private Integer id;
    private String name;
    private String description;
    @ColumnInfo(typeAffinity = INTEGER)
    @TypeConverters({DateConverter.class})
    private Date deadline;
    private int priority;

    public ToDo(String name, String description, Date deadline, int priority) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
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

    public String getStrPriority() {
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
