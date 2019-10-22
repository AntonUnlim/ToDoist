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
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Presenter.Const;
import com.unlim.todoist.R;

public class ToDoActivity extends AppCompatActivity {

    private AddEditToDoFrag addEditToDoFrag;
    private ViewToDoFrag viewToDoFrag;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private boolean isToDoAdd;
    private boolean isToDoView;

    private ToDo currentToDo;
    private Database database;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        isToDoAdd = getIntent().getBooleanExtra(Const.INTENT_IS_TODO_ADD, true);
        isToDoView = getIntent().getBooleanExtra(Const.INTENT_IS_TODO_VIEW, false);
        if (!isToDoAdd) {
            currentToDo = (ToDo)getIntent().getSerializableExtra(Const.INTENT_SELECTED_TODO);
        }
        initUI();
        database = new Database(getContentResolver());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isToDoView && !isToDoAdd) {
            if (menu != null) {
                menu.setGroupVisible(R.id.todo_menu_group, true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (isToDoView && !isToDoAdd) {
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
        addEditToDoFrag.setIsToDoAdd(isToDoAdd);
        viewToDoFrag = new ViewToDoFrag();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();

        if (isToDoView) {
            showViewFragment();
            viewToDoFrag.setCurrentToDo(currentToDo);
        } else {
            if (isToDoAdd) {
                showAddEditFragment();
            } else {
                addEditToDoFrag.setCurrentToDo(currentToDo);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                if(isToDoView) {
                    this.finish();
                } else {
                    showViewFragment();
                }
                return true;
            case R.id.todo_delete:
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
                        onDeleteToDoFromDB();
                        finish();
                    }
                });
                dialog.show();
                return true;
            case R.id.todo_edit:
                showAddEditFragment();
                addEditToDoFrag.setIsToDoAdd(false);
                addEditToDoFrag.setCurrentToDo(currentToDo);
                showEditDeleteMenu(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onDeleteToDoFromDB() {
        int result = database.deleteToDoFromDB(currentToDo);
        if (result > 0) {
            Toast.makeText(this, "ToDo deleted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        database = null;
        super.onDestroy();
    }

    public void showAddEditFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_container, addEditToDoFrag);
        if (isToDoView && !isToDoAdd) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void showViewFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_container, viewToDoFrag);
        fragmentTransaction.commit();
    }
}
