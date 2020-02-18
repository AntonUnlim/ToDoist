package com.unlim.todoist.Presenter;

import android.content.Intent;

import com.unlim.todoist.Model.DatabaseRoom;
import com.unlim.todoist.Model.ToDo;

public interface IToDoPresenter {
    void setDatabase(DatabaseRoom database);
    void setIntent(Intent intent);
    void setCurrentToDo(ToDo toDo);
    void deleteToDoFromDB(ToDo toDo);
    void onEditClicked();
    void onDeleteClicked();
    boolean isCreateEditDeleteMenu();
    boolean isToDoAdd();
    void showFragment();
}
