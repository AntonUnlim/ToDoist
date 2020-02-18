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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.unlim.todoist.Model.DatabaseRoom;
import com.unlim.todoist.Model.NetworkService;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Presenter.Const;
import com.unlim.todoist.Presenter.IToDoListPresenter;
import com.unlim.todoist.Presenter.ToDoListPresenter;
import com.unlim.todoist.Presenter.ToDoNotification;
import com.unlim.todoist.R;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements IToDoListView, ServiceConnection {
    private IToDoListPresenter toDoListPresenter;
    private ProgressBar toDoListProgressBar;
    private RecyclerView toDoRecyclerView;
    private boolean isBound;
    private ToDoNotification toDoNotification;
    private ToDoListAdapter toDoListAdapter;
    private List<ToDo> toDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        initUI();
        toDoListPresenter = new ToDoListPresenter(this);
        toDoListPresenter.setDatabase(new DatabaseRoom(getApplicationContext()));
        toDoNotification = new ToDoNotification(this);
        toDoListPresenter.setNotifications(toDoNotification);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        setProgressBarVisible(true);
        boolean isReadToDosFromDB = getIntent().getBooleanExtra(Const.INTENT_IS_READ_FROM_SERVER, false);
        if (isReadToDosFromDB) {
            Intent intentService = new Intent(this, NetworkService.class);
            isBound = bindService(intentService, this, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setProgressBarVisible(false);
        toDoListPresenter.getToDoListFromDB();
        fillListView(toDoList);
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
        toDoNotification = null;
        toDoListPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if (isBound) {
            unbindService(this);
            isBound = false;
        }
        getIntent().putExtra(Const.INTENT_IS_READ_FROM_SERVER, false);
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
        toDoListAdapter = new ToDoListAdapter();
        toDoListAdapter.setItems(toDoList);
        toDoListAdapter.setOnRecyclerItemClickListener(getOnItemClickListener());
        toDoRecyclerView.setAdapter(toDoListAdapter);
    }

    private ToDoListAdapter.OnRecyclerItemClickListener getOnItemClickListener() {
        return new ToDoListAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(ToDo toDo) {
                Intent intent = new Intent(getApplicationContext(), ToDoActivity.class);
                intent.putExtra(Const.INTENT_SELECTED_TODO, toDo);
                intent.putExtra(Const.INTENT_IS_TODO_ADD, false);
                intent.putExtra(Const.INTENT_IS_TODO_VIEW, true);
            startActivity(intent);
            }
        };
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void getToDoListFromDB(List<ToDo> toDoList) {
        this.toDoList = toDoList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu:
                Intent intent = new Intent(this, ToDoActivity.class);
                intent.putExtra(Const.INTENT_IS_TODO_ADD, true);
                intent.putExtra(Const.INTENT_IS_TODO_VIEW, false);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
