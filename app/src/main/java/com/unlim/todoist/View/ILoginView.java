package com.unlim.todoist.View;

public interface ILoginView {
    void onLoginResult(boolean resultOk, String message);
    void onLoginButtonEnabled(boolean isEnabled);
}
