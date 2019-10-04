package com.unlim.todoist.View;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.unlim.todoist.Model.NetworkService;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Presenter.IToDoListPresenter;
import com.unlim.todoist.Presenter.ToDoListPresenter;
import com.unlim.todoist.R;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements IToDoListView, ServiceConnection {
    private IToDoListPresenter toDoListPresenter;
    private ProgressBar toDoListProgressBar;
    private RecyclerView toDoRecyclerView;
    private boolean isBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        initUI();
        toDoListPresenter = new ToDoListPresenter(this);
        toDoListPresenter.setDatabase(this);
        toDoListPresenter.setNotifications(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        setProgressBarVisible(true);
        Intent intentService = new Intent(this, NetworkService.class);
        isBound = bindService(intentService, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void getToDoListFromServiceResult(List<ToDo> toDoList) {
        setProgressBarVisible(false);
        fillListView(toDoList);
    }

    @Override
    public void setProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            toDoRecyclerView.setVisibility(View.INVISIBLE);
            toDoListProgressBar.setVisibility(View.VISIBLE);
        } else {
            toDoRecyclerView.setVisibility(View.VISIBLE);
            toDoListProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        toDoListPresenter.onDestroy();
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
        toDoListPresenter.setNetworkService(binder.getService());
        if (isBound) {
            toDoListPresenter.getToDoListFromService(1);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        isBound = false;
    }

    private void initUI() {
        toDoListProgressBar = (ProgressBar) findViewById(R.id.todolist_progress_bar);
        toDoRecyclerView = (RecyclerView) findViewById(R.id.todolist_recycler_view);
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fillListView(List<ToDo> toDoList) {
        ToDoListAdapter toDoListAdapter = new ToDoListAdapter();
        toDoListAdapter.setItems(toDoList);
        toDoRecyclerView.setAdapter(toDoListAdapter);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
