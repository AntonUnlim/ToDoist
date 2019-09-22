package com.unlim.todoist.Presenter;

import com.unlim.todoist.Model.IToDoListModel;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Model.ToDoListResponse;
import com.unlim.todoist.View.IToDoListView;

import java.util.List;

public class ToDoListPresenter implements IToDoListPresenter, IToDoListModel.OnGetToDoList {
    private IToDoListView toDoListView;
    private IToDoListModel networkService;
    private List<ToDo> currentToDoList;
    private List<Integer> toDoListToDelete;

    public ToDoListPresenter (IToDoListView toDoListView) {
        this.toDoListView = toDoListView;
    }

    @Override
    public void onSuccess(ToDoListResponse toDoListResponse) {
        if (toDoListView != null) {
            currentToDoList = toDoListResponse.getCurrentList();
            toDoListToDelete = toDoListResponse.getDeleteList();
            toDoListView.onToDoListResult(currentToDoList);
        }
    }

    @Override
    public void onFailure(Throwable t) { }

    @Override
    public void setNetworkService(IToDoListModel networkService) {
        this.networkService = networkService;
    }

    @Override
    public void getToDoList(int userID) {
        if (networkService != null) {
            networkService.getToDoList(this, userID);
        }
    }

    @Override
    public void onDestroy() {
        this.toDoListView = null;
        this.networkService = null;
    }
}
