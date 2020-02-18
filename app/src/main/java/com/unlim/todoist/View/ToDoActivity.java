package com.unlim.todoist.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.DatabaseRoom;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Presenter.IToDoPresenter;
import com.unlim.todoist.Presenter.ToDoPresenter;
import com.unlim.todoist.R;

public class ToDoActivity extends AppCompatActivity implements IToDoActivity {

    private AddEditToDoFrag addEditToDoFrag;
    private ViewToDoFrag viewToDoFrag;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private ToDo currentToDo;

    private Menu menu;

    private IToDoPresenter toDoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        toDoPresenter = new ToDoPresenter(this);
        toDoPresenter.setDatabase(new DatabaseRoom(this));
        toDoPresenter.setIntent(getIntent());

        initUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (toDoPresenter.isCreateEditDeleteMenu()) {
            if (menu != null) {
                menu.setGroupVisible(R.id.todo_menu_group, true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (toDoPresenter.isCreateEditDeleteMenu()) {
            getMenuInflater().inflate(R.menu.todo_menu, menu);
        }
        return true;
    }

    public void showEditDeleteMenu(boolean isVisible) {
        if (menu != null) {
            menu.setGroupVisible(R.id.todo_menu_group, isVisible);
        }
    }

    private void initUI() {
        addEditToDoFrag = new AddEditToDoFrag();
        viewToDoFrag = new ViewToDoFrag();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();
        toDoPresenter.showFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.todo_delete:
                toDoPresenter.onDeleteClicked();
                return true;
            case R.id.todo_edit:
                toDoPresenter.onEditClicked();
                showEditDeleteMenu(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showViewToDoFrag(ToDo toDo) {
        currentToDo = toDo;
        viewToDoFrag.setCurrentToDo(toDo);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_container, viewToDoFrag);
        fragmentTransaction.commit();
    }

    @Override
    public void showEditToDoFrag(ToDo toDo) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_container, addEditToDoFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        currentToDo = toDo;
        addEditToDoFrag.setCurrentToDo(toDo);
        addEditToDoFrag.setIsToDoAdd(false);
    }

    @Override
    public void showAddToDoFrag() {
        addEditToDoFrag.setIsToDoAdd(toDoPresenter.isToDoAdd());
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_container, addEditToDoFrag);
        fragmentTransaction.commit();
    }

    @Override
    public void showAlertDialogOnDelete(final ToDo toDo) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getString(R.string.attention));
        dialog.setMessage(getString(R.string.are_you_sure_delete_todo));
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toDoPresenter.deleteToDoFromDB(toDo);
                finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void setCurrentToDo(ToDo toDo) {
        currentToDo = toDo;
        toDoPresenter.setCurrentToDo(toDo);
    }

    public ToDo getCurrentToDo() {
        return currentToDo;
    }
}
