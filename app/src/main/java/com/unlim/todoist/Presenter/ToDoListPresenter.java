package com.unlim.todoist.Presenter;

import com.unlim.todoist.Model.DatabaseRoom;
import com.unlim.todoist.Model.IToDoListModel;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.ToDoListResponse;
import com.unlim.todoist.View.IToDoListView;

import java.util.List;

public class ToDoListPresenter implements IToDoListPresenter, IToDoListModel.OnGetToDoList {
    private IToDoListView toDoListView;
    private IToDoListModel networkService;
    //private Database database;
    private DatabaseRoom database;
    private ToDoNotification toDoNotification;

    public ToDoListPresenter (IToDoListView toDoListView) {
        this.toDoListView = toDoListView;
    }

    @Override
    public void onSuccess(ToDoListResponse toDoListResponse) {
        if (toDoListView != null) {
            List<ToDo> currentToDoList = toDoListResponse.getCurrentList();
            List<Integer> toDoListToDelete = toDoListResponse.getDeleteList();
            if (database != null) {
                database.deleteToDoListFromDB(toDoListToDelete);
                int insertResult = database.saveToDoListToDB(currentToDoList);
                if (insertResult == -1) {
                    toDoListView.showToast("Something went wrong!");
                }
                toDoListView.getToDoListFromServiceResult(database.getToDoListFromDB());
                notExpiredToDosNotify();
            }
        }
    }

    @Override
    public void onFailure(Throwable t) { }

    @Override
    public void setNetworkService(IToDoListModel networkService) {
        this.networkService = networkService;
    }

    @Override
    public void setDatabase(DatabaseRoom database) {
        this.database = database;

    }

    @Override
    public void setNotifications(ToDoNotification toDoNotification) {
        this.toDoNotification = toDoNotification;
    }

    @Override
    public void getToDoListFromService(int userID) {
        if (networkService != null) {
            networkService.getToDoList(this, userID);
        }
    }

    @Override
    public void onDestroy() {
        this.toDoListView = null;
        this.networkService = null;
        this.database = null;
        this.toDoNotification = null;
    }

    @Override
    public void getToDoListFromDB() {
        List<ToDo> temp = database.getToDoListFromDB();
        toDoListView.getToDoListFromDB(temp);
    }

    private void notExpiredToDosNotify() {
        List<ToDo> notExpiredToDoList = database.getNotExpiredToDos();
        if (!notExpiredToDoList.isEmpty()) {
            for (ToDo toDo : notExpiredToDoList) {
                if (toDoNotification != null) {
                    toDoNotification.create(toDo.getId(), toDo.getName(), toDo.getDescription());
                }
            }
        }
    }
}
