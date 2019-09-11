package com.unlim.todoist.Presenter;

import com.unlim.todoist.Model.ILoginModel;

public interface ILoginPresenter {
    void setNetworkService(ILoginModel loginModel);
    void tryToLogin(String login, String password);
    void onDestroy();
}
