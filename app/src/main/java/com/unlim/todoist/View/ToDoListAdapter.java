package com.unlim.todoist.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unlim.todoist.Model.ToDo;
import com.unlim.todoist.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoViewHolder> {

    private List<ToDo> toDoList = new ArrayList<>();
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public interface OnRecyclerItemClickListener {
        void onItemClick(ToDo toDo);
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {
        private TextView idTextView;
        private TextView nameTextView;
        private TextView deadlineTextView;
        private TextView priorityTextView;
        private TextView descriptionTextView;

        public ToDoViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.tv_todo_id);
            nameTextView = itemView.findViewById(R.id.todo_name_text_view);
            deadlineTextView = itemView.findViewById(R.id.todo_deadline_text_view);
            priorityTextView = itemView.findViewById(R.id.todo_priority_value_text_view);
            descriptionTextView = itemView.findViewById(R.id.todo_description_text_view);
        }

        public void bind(final ToDo currentToDo, final OnRecyclerItemClickListener onRecyclerItemClickListener) {
            idTextView.setText("ID: " + String.valueOf(currentToDo.getId()));
            nameTextView.setText(currentToDo.getName());
            deadlineTextView.setText(currentToDo.getDeadlineStr());
            priorityTextView.setText(currentToDo.getStrPriority());
            descriptionTextView.setText(currentToDo.getDescription());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerItemClickListener.onItemClick(currentToDo);
                }
            });
        }
    }

    public void setItems(Collection<ToDo> toDos) {
        toDoList.addAll(toDos);
        notifyDataSetChanged();
    }

    public void setOnRecyclerItemClickListener (OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
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
        holder.bind(toDoList.get(position), onRecyclerItemClickListener);
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }
}
