package com.unlim.todoist.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.R;

import java.util.List;

public class ToDoListAdapter extends ArrayAdapter<ToDo> {
    private List<ToDo> toDoList;
    private int toDoListItemLayout;
    private LayoutInflater inflater;

    public ToDoListAdapter(Context context, int resource, List<ToDo> todos) {
        super(context, resource, todos);
        this.toDoList = todos;
        this.toDoListItemLayout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View currentView, ViewGroup parent) {
        View view = inflater.inflate(this.toDoListItemLayout, parent, false);
        TextView nameTextView = (TextView)view.findViewById(R.id.todo_name_text_view);
        TextView deadlineTextView = (TextView)view.findViewById(R.id.todo_deadline_text_view);
        TextView priorityTextView = (TextView)view.findViewById(R.id.todo_priority_value_text_view);
        TextView descriptionTextView = (TextView)view.findViewById(R.id.todo_description_text_view);

        ToDo currentToDo = toDoList.get(position);

        nameTextView.setText(currentToDo.getName());
        deadlineTextView.setText(currentToDo.getDeadline());
        priorityTextView.setText(currentToDo.getPriority());
        descriptionTextView.setText(currentToDo.getDescription());

        return view;
    }
}
