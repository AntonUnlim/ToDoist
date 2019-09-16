package com.unlim.todoist.Model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkConnection {
    private static final String BASE_URL = "http://demo0283530.mockable.io/todoist/";

    public static ILoginAPI getLoginAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ILoginAPI loginAPI = retrofit.create(ILoginAPI.class);
        return loginAPI;
    }
}
