package com.unlim.todoist.Model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.unlim.todoist.Presenter.Const;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Database {
    // test changes for GItHub
    private ContentResolver contentResolver;
    static final String TODO_TABLE_NAME = "ToDo";
    static final String AUTHORITY = "com.unlim.todoist.provider";
    private final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TODO_TABLE_NAME;
    static final String CONTENT_ITEM_TYPE= "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TODO_TABLE_NAME;

    static class Columns {
        static final String TODO_ID = "_id";
        static final String TODO_NAME = "Name";
        static final String TODO_DESCRIPTION = "Description";
        static final String TODO_DEADLINE = "Deadline";
        static final String TODO_PRIORITY = "Priority";

        private Columns(){}
    }

    public Database(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    private final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TODO_TABLE_NAME);

    Uri buildToDoUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    long getToDoID(Uri uri) {
        return ContentUris.parseId(uri);
    }

    public int saveToDoListToDB(List<ToDo> newToDoList) {
        int rowsCount = 0;
        for(ToDo newToDo : newToDoList) {
            saveToDoToDB(newToDo);
        }
        return rowsCount;
    }

    public List<ToDo> getToDoListFromDB() {
        List<ToDo> returnList = new ArrayList<>();
        String[] projection = {
                Columns.TODO_ID,
                Columns.TODO_NAME,
                Columns.TODO_DESCRIPTION,
                Columns.TODO_DEADLINE,
                Columns.TODO_PRIORITY
        };
        Cursor cursor = contentResolver.query(CONTENT_URI,
                projection,
                null,
                null,
                Columns.TODO_ID);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(Columns.TODO_ID));
                String name = cursor.getString(cursor.getColumnIndex(Columns.TODO_NAME));
                String description = cursor.getString(cursor.getColumnIndex(Columns.TODO_DESCRIPTION));
                Date deadline = null;
                try {
                    deadline = new SimpleDateFormat(Const.DATE_FORMAT).parse(cursor.getString(cursor.getColumnIndex(Columns.TODO_DEADLINE)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int priority = cursor.getInt(cursor.getColumnIndex(Columns.TODO_PRIORITY));
                ToDo newToDo = new ToDo(name, description, deadline, priority);
                newToDo.setId(id);
                returnList.add(newToDo);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return returnList;
    }

    public void deleteToDoListFromDB(List<Integer> listToDelete) {
        for(Integer i : listToDelete) {
            contentResolver.delete(buildToDoUri(i), null, null);
        }
    }

    public int deleteToDoFromDB(ToDo toDo) {
        return contentResolver.delete(buildToDoUri(toDo.getId()), null, null);
    }

    private boolean isExistsByID(List<ToDo> list, ToDo toDo) {
        for(ToDo existingToDo : list) {
            if (existingToDo.getId() == toDo.getId()) return true;
        }
        return false;
    }

    public List<ToDo> getNotExpiredToDos() {
        List<ToDo> currentToDoList = getToDoListFromDB();
        List<ToDo> notExpiredList = new ArrayList<>();
        for (ToDo toDo : currentToDoList) {
            if (!toDo.isExpired()) {
                notExpiredList.add(toDo);
            }
        }
        return notExpiredList;
    }

    private ToDo getToDoByID(List<ToDo> toDos, int id) {
        for (ToDo toDo : toDos) {
            if(toDo.getId() == id) return toDo;
        }
        return null;
    }

    public int saveToDoToDB(ToDo newToDo) {
        int rowsCount = 0;
        ContentValues contentValues = new ContentValues();
        List<ToDo> currentToDoList = getToDoListFromDB();
        if (isExistsByID(currentToDoList, newToDo)) {
            ToDo oldToDo = getToDoByID(currentToDoList, newToDo.getId());
            if (!newToDo.getName().equals(oldToDo.getName()) ||
                    !Objects.equals(newToDo.getDescription(), oldToDo.getDescription()) ||
                    !newToDo.getDeadlineStr().equals(oldToDo.getDeadlineStr()) ||
                    !newToDo.getPriority().equals(oldToDo.getPriority())) {
                contentValues.put(Columns.TODO_NAME, newToDo.getName());
                contentValues.put(Columns.TODO_DESCRIPTION, newToDo.getDescription());
                contentValues.put(Columns.TODO_DEADLINE, newToDo.getDeadlineStr());
                contentValues.put(Columns.TODO_PRIORITY, newToDo.getIntPriority());
                String selection = Columns.TODO_ID + " = ?";
                String[] args = {String.valueOf(newToDo.getId())};
                rowsCount = contentResolver.update(CONTENT_URI, contentValues, selection, args);
            }
        } else {
            contentValues.put(Columns.TODO_NAME, newToDo.getName());
            contentValues.put(Columns.TODO_DESCRIPTION, newToDo.getDescription());
            contentValues.put(Columns.TODO_DEADLINE, newToDo.getDeadlineStr());
            contentValues.put(Columns.TODO_PRIORITY, newToDo.getIntPriority());
            ContentValues[] cvArr = new ContentValues[1];
            if (newToDo.getId() == 0) {
                cvArr[0] = contentValues;
            } else {
                contentValues.put(Columns.TODO_ID, newToDo.getId());
                cvArr[0] = contentValues;
            }
            long newID = contentResolver.bulkInsert(CONTENT_URI, cvArr);
            if (newID > 0) {
                newToDo.setId((int) newID);
                currentToDoList.add(newToDo);
                rowsCount = 1;
            } else {
                rowsCount = -1;
            }
        }
        return rowsCount;
    }
}
