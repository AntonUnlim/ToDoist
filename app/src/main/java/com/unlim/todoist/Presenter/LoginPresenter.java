package com.unlim.todoist.Presenter;

import com.unlim.todoist.Model.ILoginModel;
import com.unlim.todoist.Model.LoginResponse;
import com.unlim.todoist.View.ILoginView;

public class LoginPresenter implements ILoginPresenter, ILoginModel.OnLogin {
    private ILoginView loginView;
    private ILoginModel networkService;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
    }

    public void setNetworkService(ILoginModel networkService) {
        this.networkService = networkService;
    }

    @Override
    public void tryToLogin(String login, String password) {
        networkService.tryToLogin(this, login, password);
    }

    @Override
    public void onDestroy() {
        this.loginView = null;
    }

    @Override
    public void onSuccess(LoginResponse loginResponse) {
        if (loginResponse.getErrCode() == 200 && !loginResponse.getToken().isEmpty()) {
            loginView.onLoginResult(true, Integer.toString(loginResponse.getErrCode()));
        } else {
            loginView.onLoginResult(false, loginResponse.getErrMessage());
        }
    }

    @Override
    public void onFailure(Throwable t) {
        loginView.onLoginResult(false, t.getMessage());
    }
}
