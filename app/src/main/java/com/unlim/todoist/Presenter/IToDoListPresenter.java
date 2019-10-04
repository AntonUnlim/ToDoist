package com.unlim.todoist.Presenter;

import android.content.Context;

import com.unlim.todoist.Model.IToDoListModel;

public interface IToDoListPresenter {
    void setNetworkService(IToDoListModel toDoListModel);
    void setDatabase(Context context);
    void setNotifications(Context context);
    void getToDoListFromService(int userID);
    void onDestroy();
}
