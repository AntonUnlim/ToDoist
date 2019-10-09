package com.unlim.todoist.Presenter;

import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.IToDoListModel;

public interface IToDoListPresenter {
    void setNetworkService(IToDoListModel toDoListModel);
    void setDatabase(Database database);
    void setNotifications(ToDoNotification toDoNotification);
    void getToDoListFromService(int userID);
    void onDestroy();
}
