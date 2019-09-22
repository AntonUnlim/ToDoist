package com.unlim.todoist.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IToDoListAPI {
    @GET("all")
    Call<ToDoListResponse> getToDoListResponse(@Query("userid") int userID);
}
