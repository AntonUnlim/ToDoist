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

public class LoginActivity extends AppCompatActivity implements ILoginView, ServiceConnection {

    private ILoginPresenter loginPresenter;
    private EditText etLogin;
    private EditText etPassword;
    private Button btnSingIn;
    private boolean isBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        loginPresenter = new LoginPresenter(this);

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
        isBound = bindService(intentService, this, Context.BIND_AUTO_CREATE);
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
        loginPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if (isBound) {
            unbindService(this);
            isBound = false;
        }
        super.onStop();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        NetworkService.LocalBinder binder = (NetworkService.LocalBinder) service;
        loginPresenter.setNetworkService(binder.getService());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        isBound = false;
    }
}
