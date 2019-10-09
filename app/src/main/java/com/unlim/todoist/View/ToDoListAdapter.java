package com.unlim.todoist.View;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoViewHolder> {

    private List<ToDo> toDoList = new ArrayList<>();

    class ToDoViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView deadlineTextView;
        private TextView priorityTextView;
        private TextView descriptionTextView;

        public ToDoViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.todo_name_text_view);
            deadlineTextView = itemView.findViewById(R.id.todo_deadline_text_view);
            priorityTextView = itemView.findViewById(R.id.todo_priority_value_text_view);
            descriptionTextView = itemView.findViewById(R.id.todo_description_text_view);
        }

        public void bind(ToDo currentToDo) {
            nameTextView.setText(currentToDo.getName());
            deadlineTextView.setText(currentToDo.getDeadline());
            priorityTextView.setText(currentToDo.getPriority());
            descriptionTextView.setText(currentToDo.getDescription());
            //if (currentToDo.isExpired()) {
            //    view.setBackgroundColor(Color.rgb(255, 157, 160));
            //}
        }
    }

    public void setItems(Collection<ToDo> toDos) {
        toDoList.addAll(toDos);
        notifyDataSetChanged();
    }

    public void clearItems() {
        toDoList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todolist_item, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToDoViewHolder holder, int position) {
        holder.bind(toDoList.get(position));
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }
}
