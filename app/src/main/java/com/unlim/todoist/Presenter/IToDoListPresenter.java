package com.unlim.todoist.Presenter;

import com.unlim.todoist.Model.IToDoListModel;

public interface IToDoListPresenter {
    void setNetworkService(IToDoListModel toDoListModel);
    void getToDoList(int userID);
    void onDestroy();
}
