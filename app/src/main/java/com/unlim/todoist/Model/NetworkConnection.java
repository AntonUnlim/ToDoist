package com.unlim.todoist.Model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkConnection {
    private static final String BASE_URL = "https://demo0283530.mockable.io/todoist/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ILoginAPI getLoginAPI() {
        ILoginAPI loginAPI = retrofit.create(ILoginAPI.class);
        return loginAPI;
    }

    public static IToDoListAPI getToDoListAPI() {
        IToDoListAPI toDoListAPI = retrofit.create(IToDoListAPI.class);
        return toDoListAPI;
    }
}
