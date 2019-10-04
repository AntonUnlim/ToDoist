package com.unlim.todoist.Presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unlim.todoist.Model.Database;
import com.unlim.todoist.Model.ToDo;

import java.util.List;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Database database = new Database(context.getContentResolver());
            List<ToDo> notExpiredToDoList = database.getNotExpiredToDos();
            if (!notExpiredToDoList.isEmpty()) {
                ToDoNotification toDoNotification = new ToDoNotification(context);
                for (ToDo toDo : notExpiredToDoList) {
                    toDoNotification.create(toDo.getId(), toDo.getName(), toDo.getDescription());
                }
            }
        }
    }
}
