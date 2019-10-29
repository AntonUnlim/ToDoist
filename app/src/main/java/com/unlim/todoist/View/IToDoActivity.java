package com.unlim.todoist.View;

import com.unlim.todoist.Model.ToDo;

public interface IToDoActivity {
    void showToast(String text);
    void showViewToDoFrag(ToDo toDo);
    void showEditToDoFrag(ToDo toDo);
    void showAddToDoFrag();
    void showAlertDialogOnDelete(ToDo toDo);
}
