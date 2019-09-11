package com.unlim.todoist.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ILoginAPI {
    @GET("login")
    Call<LoginResponse> getLoginResponse(@Query("login") String login, @Query("password") String password);
}
