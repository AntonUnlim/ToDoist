package com.unlim.todoist.Model;

public interface IToDoListModel {
    interface OnGetToDoList {
        void onSuccess(ToDoListResponse toDoListResponse);
        void onFailure(Throwable t);
    }
    void getToDoList(OnGetToDoList onConnect, int userID);
}
