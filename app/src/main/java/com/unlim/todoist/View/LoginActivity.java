package com.unlim.todoist.View;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unlim.todoist.Model.NetworkService;
import com.unlim.todoist.Presenter.ILoginPresenter;
import com.unlim.todoist.Presenter.LoginPresenter;
import com.unlim.todoist.R;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    private ILoginPresenter loginPresenter;
    private EditText etLogin;
    private EditText etPassword;
    private Button btnSingIn;
    private NetworkService networkService;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                if (!login.isEmpty() && !password.isEmpty()) {
                    if (isBound) {
                        onLoginButtonEnabled(false);
                        loginPresenter.tryToLogin(login, password);
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intentService = new Intent(this, NetworkService.class);
        bindService(intentService, connection, Context.BIND_AUTO_CREATE);
        loginPresenter = new LoginPresenter(this);
    }

    @Override
    public void onLoginResult(boolean resultOk, String message) {
        if (resultOk) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            onLoginButtonEnabled(true);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoginButtonEnabled(boolean isEnabled) {
        btnSingIn.setEnabled(isEnabled);
    }

    private void initUI() {
        etLogin = (EditText)findViewById(R.id.et_login);
        etPassword = (EditText)findViewById(R.id.et_password);
        btnSingIn = (Button)findViewById(R.id.btn_sign_in);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loginPresenter.onDestroy();
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            NetworkService.LocalBinder binder = (NetworkService.LocalBinder) service;
            networkService = binder.getService();
            loginPresenter.setNetworkService(networkService);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
}
