package com.unlim.todoist.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class AddEditToDoFrag extends Fragment implements IAddEditToDoView {

    private IAddEditToDoPresenter addEditToDoPresenter;
    private Database database;
    private Button btnSave, btnCancel;
    private EditText etToDoName, etToDoDescription;
    private Spinner spinPriority;
    private TextView tvDeadline;
    private String[] priorities = new String[] {"L", "H"};
    private int currYear;
    private int currMonth;
    private int currDay;
    private boolean isToDoAdd;
    private ToDo currentToDo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.add_edit_todo_frag, null);
        initUI(v);

        addEditToDoPresenter = new AddEditToDoPresenter(this);
        database = new Database(getActivity().getContentResolver());
        addEditToDoPresenter.setDatabase(database);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isToDoAdd) {
                    getActivity().finish();
                } else {
                    ((ToDoActivity)getActivity()).showViewFragment();
                }
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

        return v;
    }

    @Override
    public void setCurrentToDo(ToDo toDo) {
        this.currentToDo = toDo;
    }

    @Override
    public void setIsToDoAdd(boolean isToDoAdd) {
        this.isToDoAdd = isToDoAdd;
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        database = null;
        addEditToDoPresenter.onDestroy();
        super.onDestroy();
    }

    private void initUI(View v) {
        btnCancel = v.findViewById(R.id.btn_todo_cancel);
        btnSave = v.findViewById(R.id.btn_todo_save);
        etToDoName = v.findViewById(R.id.et_todo_name);
        etToDoDescription = v.findViewById(R.id.et_todo_description);
        spinPriority = v.findViewById(R.id.spin_priority);
        tvDeadline = v.findViewById(R.id.tv_deadline_value);

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, priorities);
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
            spinPriority.setSelection(currentToDo.getIntPriority());
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
        if (isToDoAdd) {
            getActivity().finish();
        } else {
            ((ToDoActivity) getActivity()).showViewFragment();
        }
    }
}
