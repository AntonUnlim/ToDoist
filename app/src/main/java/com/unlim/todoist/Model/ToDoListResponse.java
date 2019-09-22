package com.unlim.todoist.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ToDoListResponse {
    @SerializedName("currentList")
    private List<ToDo> currentList;

    @SerializedName("deleteList")
    private List<Integer> deleteList;

    public List<ToDo> getCurrentList() {
        return currentList;
    }

    public void setCurrentList(List<ToDo> currentList) {
        this.currentList = currentList;
    }

    public List<Integer> getDeleteList() {
        return deleteList;
    }

    public void setDeleteList(List<Integer> deleteList) {
        this.deleteList = deleteList;
    }
}
