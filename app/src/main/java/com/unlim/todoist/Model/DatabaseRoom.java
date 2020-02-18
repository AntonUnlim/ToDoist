package com.unlim.todoist.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DatabaseRoom {

    private ToDoDao toDoDao;

    public DatabaseRoom(Context context) {
        ToDoRoomDatabase database = ToDoRoomDatabase.getDatabase(context);
        toDoDao = database.toDoDao();
    }

    public List<ToDo> getToDoListFromDB() {
        return toDoDao.getAll();
    }

    public void deleteToDoListFromDB(List<Integer> listToDelete) {
        for(Integer i : listToDelete) {
            ToDo tempToDo = toDoDao.getById(i);
            if (tempToDo != null) {
                toDoDao.delete(tempToDo);
            }
        }
    }

    public int saveToDoListToDB(List<ToDo> newToDoList) {
        int rowsCount = 0;
        for(ToDo newToDo : newToDoList) {
            toDoDao.insert(newToDo);
        }
        return rowsCount;
    }

    public int saveToDoToDB(ToDo newToDo) {
        return (int)toDoDao.insert(newToDo);
    }

    public List<ToDo> getNotExpiredToDos() {
        List<ToDo> fullList = toDoDao.getAll();
        List<ToDo> notExpiredList = new ArrayList<>();
        for (ToDo toDo : fullList) {
            if (!toDo.isExpired()) {
                notExpiredList.add(toDo);
            }
        }
        return notExpiredList;
    }

    public int deleteToDoFromDB(ToDo toDo) {
        return toDoDao.delete(toDo);
    }
}
