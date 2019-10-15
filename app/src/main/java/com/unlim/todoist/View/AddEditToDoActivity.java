package com.unlim.todoist.View;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.Presenter.AddEditToDoPresenter;
import com.unlim.todoist.Presenter.Const;
import com.unlim.todoist.Presenter.IAddEditToDoPresenter;
import com.unlim.todoist.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditToDoActivity extends AppCompatActivity implements IAddEditToDoView {

    private IAddEditToDoPresenter addEditToDoPresenter;
    private Database database;

    private TextView tvDeadline;
    private EditText etToDoName, etToDoDescription;
    private Spinner spinPriority;
    private ImageButton btnShowCalendar;
    private Button btnSave, btnCancel;
    private String[] priorities = new String[] {"L", "H"};
    private int DIALOG_DATE = 1;
    private boolean isToDoAdd;
    private ToDo currentToDo;

    private int currYear;
    private int currMonth;
    private int currDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_to_do);

        isToDoAdd = getIntent().getBooleanExtra(Const.INTENT_IS_TODO_ADD, true);
        if (!isToDoAdd) {
            currentToDo = (ToDo)getIntent().getSerializableExtra(Const.INTENT_TODO_EDIT);
        }

        initUI();
        addEditToDoPresenter = new AddEditToDoPresenter(this);
        database = new Database(this.getContentResolver());
        addEditToDoPresenter.setDatabase(database);

        btnShowCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE); // deprecated
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etToDoName.getText().toString().isEmpty() || !etToDoName.getText().toString().trim().equals("")) {
                    saveNewToDo();
                } else {
                    showToast("Name can't be empty");
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog dpd = new DatePickerDialog(this, datePickCallBack, currYear, currMonth, currDay);
                return dpd;
            }
        }
        return super.onCreateDialog(id); // deprecated
    }

    DatePickerDialog.OnDateSetListener datePickCallBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setDeadlineText(year, month, dayOfMonth);
        }
    };

    private void initUI() {
        tvDeadline = findViewById(R.id.tv_deadline_value);
        btnShowCalendar = findViewById(R.id.btn_get_date);
        btnSave = findViewById(R.id.btn_todo_save);
        btnCancel = findViewById(R.id.btn_todo_cancel);
        etToDoName = findViewById(R.id.et_todo_name);
        etToDoDescription = findViewById(R.id.et_todo_description);

        spinPriority = findViewById(R.id.spin_priority);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPriority.setAdapter(spinAdapter);

        if (isToDoAdd) {
            currYear = Calendar.getInstance().get(Calendar.YEAR);
            currMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            currDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        } else {
            Calendar toDoDeadline = Calendar.getInstance();
            toDoDeadline.setTime(currentToDo.getDeadline());
            currYear = toDoDeadline.get(Calendar.YEAR);
            currMonth = toDoDeadline.get(Calendar.MONTH) + 1;
            currDay = toDoDeadline.get(Calendar.DAY_OF_MONTH);
            etToDoName.setText(currentToDo.getName());
            etToDoDescription.setText(currentToDo.getDescription());
        }
        setDeadlineText(currYear, currMonth, currDay);
    }

    private void setDeadlineText(int year, int month, int day) {
        String monthStr = String.format(Locale.getDefault(),"%02d", month);
        String dayStr = String.format(Locale.getDefault(),"%02d", day);
        tvDeadline.setText(year + "-" + monthStr + "-" + dayStr);
    }

    private void saveNewToDo() {
        Date newToDoDeadline;
        try {
            newToDoDeadline = new SimpleDateFormat(Const.DATE_FORMAT).parse(tvDeadline.getText().toString());
        } catch (ParseException e) {
            newToDoDeadline = Calendar.getInstance().getTime();
            e.printStackTrace();
        }
        int priority = (spinPriority.getSelectedItem().toString().equals("H")) ? 1 : 0;
        ToDo newToDo;
        String toDoName = etToDoName.getText().toString();
        String toDoDescription = etToDoDescription.getText().toString();
        if(isToDoAdd) {
            newToDo = new ToDo(toDoName, toDoDescription, newToDoDeadline, priority);
        } else {
            newToDo = currentToDo;
            newToDo.setName(toDoName);
            newToDo.setDescription(toDoDescription);
            newToDo.setDeadline(newToDoDeadline);
            newToDo.setPriority(priority);
        }
        addEditToDoPresenter.saveToDo(newToDo);
        finish();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        database = null;
        addEditToDoPresenter.onDestroy();
        super.onDestroy();
    }
}
