package com.unlim.todoist.View;

import com.unlim.todoist.Model.ToDo;

import java.util.List;

public interface IToDoListView {
    void onToDoListResult(List<ToDo> toDoList);
    void onProgressBarEnabled(boolean isEnabled);
}
