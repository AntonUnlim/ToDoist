package com.unlim.todoist.Model;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService extends Service implements ILoginModel {
    private final String BASE_URL = "http://demo0283530.mockable.io/todoist/";
    private IBinder binder = new LocalBinder();

    @Override
    public void tryToLogin(final OnLogin onLogin, final String login, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ILoginAPI loginAPIService = retrofit.create(ILoginAPI.class);
        Call<LoginResponse> loginResponseCall = loginAPIService.getLoginResponse(login, password);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse;
                if (response.code() == 200) {
                    loginResponse = response.body();
                    loginResponse.setErrCode(response.code());
                    loginResponse.setErrMessage(response.message());
                } else {
                    loginResponse = new LoginResponse();
                    loginResponse.setErrCode(response.code());
                    loginResponse.setErrMessage(response.message());
                }
                onLogin.onSuccess(loginResponse);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                onLogin.onFailure(t);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public NetworkService getService() {
            return NetworkService.this;
        }
    }
}
