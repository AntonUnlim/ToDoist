package com.unlim.todoist.Presenter;

import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.View.IAddEditToDoView;

public class AddEditToDoPresenter implements IAddEditToDoPresenter {

    private IAddEditToDoView addEditToDoView;
    private Database database;

    public AddEditToDoPresenter(IAddEditToDoView addEditToDoView) {
        this.addEditToDoView = addEditToDoView;
    }

    @Override
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Override
    public void saveToDo(ToDo toDo) {
        int saveResult = database.saveToDoToDB(toDo);
        if (saveResult > 0) {
            addEditToDoView.showToast("ToDo saved!");
        } else {
            addEditToDoView.showToast("ToDo NOT saved!");
        }
    }

    @Override
    public void onDestroy() {
        this.addEditToDoView = null;
        this.database = null;
    }
}
