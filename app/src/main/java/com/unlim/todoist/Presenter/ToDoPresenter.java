package com.unlim.todoist.Presenter;

import android.content.Intent;

import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.DatabaseRoom;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.View.IToDoActivity;

public class ToDoPresenter implements IToDoPresenter {

    private DatabaseRoom database;
    private IToDoActivity toDoActivity;
    private ToDo currentToDo;
    private boolean isToDoAdd;
    private boolean isToDoView;

    public ToDoPresenter(IToDoActivity toDoActivity) {
        this.toDoActivity = toDoActivity;
    }

    @Override
    public void setDatabase(DatabaseRoom database) {
        this.database = database;
    }

    @Override
    public void setIntent(Intent intent) {
        isToDoAdd = intent.getBooleanExtra(Const.INTENT_IS_TODO_ADD, true);
        isToDoView = intent.getBooleanExtra(Const.INTENT_IS_TODO_VIEW, false);
        if (!isToDoAdd) {
            currentToDo = (ToDo)intent.getSerializableExtra(Const.INTENT_SELECTED_TODO);
        }
    }

    @Override
    public void setCurrentToDo(ToDo toDo) {
        currentToDo = toDo;
    }

    @Override
    public void deleteToDoFromDB(ToDo toDo) {
        int result = database.deleteToDoFromDB(toDo);
        if (result > 0) {
            toDoActivity.showToast("ToDo deleted!");
        } else {
            toDoActivity.showToast("Something went wrong!");
        }
    }

    @Override
    public void onEditClicked() {
        toDoActivity.showEditToDoFrag(currentToDo);
    }

    @Override
    public void onDeleteClicked() {
        toDoActivity.showAlertDialogOnDelete(currentToDo);
    }

    @Override
    public boolean isCreateEditDeleteMenu() {
        return isToDoView && !isToDoAdd;
    }

    @Override
    public boolean isToDoAdd() {
        return isToDoAdd;
    }

    @Override
    public void showFragment() {
        if (isToDoView) {
            toDoActivity.showViewToDoFrag(currentToDo);
        } else if (isToDoAdd) {
            toDoActivity.showAddToDoFrag();
        } else {
            toDoActivity.showEditToDoFrag(currentToDo);
        }
    }

}
