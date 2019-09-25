package com.unlim.todoist.View;

import android.content.Context;

import com.unlim.todoist.Model.ToDo;

import java.util.List;

public interface IToDoListView {
    void getToDoListFromServiceResult(List<ToDo> toDoList);
    void setProgressBarVisible(boolean isVisible);
    Context getContext();
}
