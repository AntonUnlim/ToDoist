package com.unlim.todoist.Presenter;

import com.unlim.todoist.Model.IToDoListModel;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.ToDoListResponse;
import com.unlim.todoist.View.IToDoListView;

import java.util.List;

public class ToDoListPresenter implements IToDoListPresenter, IToDoListModel.OnGetToDoList {
    private IToDoListView toDoListView;
    private IToDoListModel networkService;

    public ToDoListPresenter (IToDoListView toDoListView) {
        this.toDoListView = toDoListView;
    }

    @Override
    public void onSuccess(ToDoListResponse toDoListResponse) {
        if (toDoListView != null) {
            List<ToDo> currentToDoList = toDoListResponse.getCurrentList();
            List<Integer> toDoListToDelete = toDoListResponse.getDeleteList();
            Database.deleteToDoListFromDB(toDoListToDelete);
            Database.saveToDoListToDB(currentToDoList);
            toDoListView.getToDoListFromServiceResult(Database.getToDoListFromDB());
            notExpiredToDosNotify();
        }
    }

    @Override
    public void onFailure(Throwable t) { }

    @Override
    public void setNetworkService(IToDoListModel networkService) {
        this.networkService = networkService;
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
    }

    private void notExpiredToDosNotify() {
        ToDoNotification toDoNotification = new ToDoNotification(toDoListView.getContext());
        List<ToDo> notExpiredToDoList = Database.getNotExpiredToDos();
        if (!notExpiredToDoList.isEmpty()) {
            for (ToDo toDo : notExpiredToDoList) {
                toDoNotification.create(toDo.getId(), toDo.getName(), toDo.getDescription());
            }
        }
    }
}
