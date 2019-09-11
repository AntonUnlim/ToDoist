package com.unlim.todoist.Model;

public interface ILoginModel {
    interface OnLogin {
        void onSuccess(LoginResponse loginResponse);
        void onFailure(Throwable t);
    }
    void tryToLogin(OnLogin onLogin, String login, String password);
}
