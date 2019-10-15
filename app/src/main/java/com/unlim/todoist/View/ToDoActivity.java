package com.unlim.todoist.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Presenter.Const;
import com.unlim.todoist.R;

public class ToDoActivity extends AppCompatActivity {

    private TextView tvToDoName, tvToDoDescription, tvToDoDeadline, tvToDoPriority;
    private ToDo currentToDo;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        currentToDo = (ToDo)getIntent().getSerializableExtra(Const.INTENT_SELECTED_TODO);
        initUI();
        database = new Database(getContentResolver());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_menu, menu);
        return true;
    }

    private void initUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvToDoName = findViewById(R.id.tv_todo_name);
        tvToDoDescription = findViewById(R.id.tv_todo_description);
        tvToDoDeadline = findViewById(R.id.tv_todo_deadline);
        tvToDoPriority = findViewById(R.id.tv_todo_priority);
        if(currentToDo != null) {
            tvToDoName.setText(currentToDo.getName());
            tvToDoDescription.setText(currentToDo.getDescription());
            tvToDoDeadline.setText(currentToDo.getDeadlineStr());
            tvToDoPriority.setText(currentToDo.getPriorityFullStr());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                this.finish();
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
                Intent intent = new Intent(this, AddEditToDoActivity.class);
                intent.putExtra(Const.INTENT_IS_TODO_ADD, false);
                intent.putExtra(Const.INTENT_TODO_EDIT, currentToDo);
                startActivity(intent);
                finish();
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
}
