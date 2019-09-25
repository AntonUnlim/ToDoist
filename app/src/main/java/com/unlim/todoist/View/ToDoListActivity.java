package com.unlim.todoist.View;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.NetworkService;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Presenter.IToDoListPresenter;
import com.unlim.todoist.Presenter.ToDoListPresenter;
import com.unlim.todoist.R;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements IToDoListView, ServiceConnection {
    private IToDoListPresenter toDoListPresenter;
    private List<ToDo> toDoCurrentList;
    private ProgressBar toDoListProgressBar;
    private ListView toDoListView;
    private boolean isBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        initUI();
        toDoListPresenter = new ToDoListPresenter(this);
        Database.setContentResolver(getContentResolver());
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
        toDoCurrentList = toDoList;
        fillListView();
    }

    @Override
    public void setProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            toDoListView.setVisibility(View.INVISIBLE);
            toDoListProgressBar.setVisibility(View.VISIBLE);
        } else {
            toDoListView.setVisibility(View.VISIBLE);
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
        toDoListView = (ListView) findViewById(R.id.todolist_list_view);
    }

    private void fillListView() {
        ToDoListAdapter toDoListAdapter = new ToDoListAdapter(this, R.layout.todolist_item, toDoCurrentList);
        toDoListView.setAdapter(toDoListAdapter);
        toDoListAdapter.notifyDataSetChanged();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
