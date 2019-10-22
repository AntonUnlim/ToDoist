package com.unlim.todoist.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.R;

public class ViewToDoFrag extends Fragment {

    private TextView tvToDoName, tvToDoDescription, tvToDoDeadline, tvToDoPriority;
    private ToDo currentToDo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.view_todo_frag, null);
        initUI(v);
        if (currentToDo != null) {
            fillFields();
        }
        ((ToDoActivity)getActivity()).showEditDeleteMenu(true);
        return v;
    }

    public void setCurrentToDo(ToDo toDo) {
        this.currentToDo = toDo;
    }

    private void initUI(View v) {
        tvToDoName = v.findViewById(R.id.tv_todo_name);
        tvToDoDescription = v.findViewById(R.id.tv_todo_description);
        tvToDoDeadline = v.findViewById(R.id.tv_todo_deadline);
        tvToDoPriority = v.findViewById(R.id.tv_todo_priority);
    }

    private void fillFields() {
        tvToDoName.setText(currentToDo.getName());
        tvToDoDescription.setText(currentToDo.getDescription());
        tvToDoDeadline.setText(currentToDo.getDeadlineStr());
        tvToDoPriority.setText(currentToDo.getPriorityFullStr());
    }
}
