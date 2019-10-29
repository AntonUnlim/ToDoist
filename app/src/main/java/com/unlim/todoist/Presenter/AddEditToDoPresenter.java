package com.unlim.todoist.Presenter;

import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Model.ToDoModel;
import com.unlim.todoist.View.IAddEditToDoView;

public class AddEditToDoPresenter implements IAddEditToDoPresenter {

    private IAddEditToDoView addEditToDoView;
    private Database database;
    private ToDo currentToDo;

    public AddEditToDoPresenter(IAddEditToDoView addEditToDoView) {
        this.addEditToDoView = addEditToDoView;
    }

    @Override
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Override
    public void setCurrentToDo(ToDo toDo) {
        currentToDo = toDo;
    }

    @Override
    public ToDo getCurrentToDo() {
        return currentToDo;
    }

    @Override
    public int saveToDo(ToDoModel toDoModel) {
        if (toDoModel.getName().isEmpty()) {
            addEditToDoView.showToast("Name can't be empty!");
            return -1;
        }
        boolean isSave;
        ToDo newToDo = new ToDo(toDoModel.getName(), toDoModel.getDescription(), toDoModel.getDeadline(), toDoModel.getPriority());
        if (currentToDo == null)
        {
            isSave = true;
        } else if (!toDoModel.getName().equals(currentToDo.getName())
                    || !toDoModel.getDescription().equals(currentToDo.getDescription())
                    || !toDoModel.getDeadline().equals(currentToDo.getDeadline())
                    || toDoModel.getPriority() != currentToDo.getIntPriority()) {
            isSave = true;
            newToDo.setId(currentToDo.getId());
            currentToDo = newToDo;
            addEditToDoView.setCurrentToDo(currentToDo);

        } else {
            isSave = false;
        }

        if(isSave) {
            int saveResult = database.saveToDoToDB(newToDo);
            if (saveResult > 0) {
                addEditToDoView.showToast("ToDo saved!");
            } else {
                addEditToDoView.showToast("ToDo NOT saved!");
            }
            return saveResult;
        }
        return -1;
    }

    @Override
    public void onDestroy() {
        this.addEditToDoView = null;
        this.database = null;
    }
}
