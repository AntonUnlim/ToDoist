package com.unlim.todoist.Presenter;

import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.ToDo;

public interface IAddEditToDoPresenter {
    void setDatabase(Database database);
    void saveToDo(ToDo toDo);
    void onDestroy();
}
