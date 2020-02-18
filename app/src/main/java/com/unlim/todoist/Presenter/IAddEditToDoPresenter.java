package com.unlim.todoist.Presenter;

import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.DatabaseRoom;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Model.ToDoModel;

public interface IAddEditToDoPresenter {
    void setDatabase(DatabaseRoom database);
    void setCurrentToDo(ToDo toDo);
    ToDo getCurrentToDo();
    int saveToDo(ToDoModel toDo);
    void onDestroy();
}
