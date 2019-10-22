package com.unlim.todoist.View;

import com.unlim.todoist.Model.ToDo;

public interface IAddEditToDoView {
    void showToast(String text);
    void setIsToDoAdd(boolean isToDoAdd);
    void setCurrentToDo(ToDo toDo);
}
