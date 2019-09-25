package com.unlim.todoist.Model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Database {
    private static ContentResolver appContentResolver;
    static final String TODO_TABLE_NAME = "ToDo";
    static final String AUTHORITY = "com.unlim.todoist.provider";
    private static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

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

    public static void setContentResolver(ContentResolver contentResolver) {
        appContentResolver = contentResolver;
    }

    private static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TODO_TABLE_NAME);

    static Uri buildToDoUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    static long getToDoID(Uri uri) {
        return ContentUris.parseId(uri);
    }

    public static void saveToDoListToDB(List<ToDo> newToDoList) {
        ContentValues contentValues = new ContentValues();
        List<ToDo> currentToDoList = getToDoListFromDB();
        for(ToDo newToDo : newToDoList) {
            ToDo oldToDo = isExistsByID(currentToDoList, newToDo);
            if(oldToDo != null) {
                if (!newToDo.getName().equals(oldToDo.getName()) ||
                        !Objects.equals(newToDo.getDescription(), oldToDo.getDescription()) ||
                        !newToDo.getDeadline().equals(oldToDo.getDeadline()) ||
                        !newToDo.getPriority().equals(oldToDo.getPriority())) {
                    contentValues.put(Columns.TODO_NAME, newToDo.getName());
                    contentValues.put(Columns.TODO_DESCRIPTION, newToDo.getDescription());
                    contentValues.put(Columns.TODO_DEADLINE, newToDo.getDeadline());
                    contentValues.put(Columns.TODO_PRIORITY, newToDo.getIntPriority());
                    String selection = Columns.TODO_ID + " = ?";
                    String[] args = {String.valueOf(newToDo.getId())};
                    appContentResolver.update(CONTENT_URI, contentValues, selection, args);
                    currentToDoList = getToDoListFromDB();
                }
            } else {
                currentToDoList.add(newToDo);
                contentValues.put(Columns.TODO_NAME, newToDo.getName());
                contentValues.put(Columns.TODO_DESCRIPTION, newToDo.getDescription());
                contentValues.put(Columns.TODO_DEADLINE, newToDo.getDeadline());
                contentValues.put(Columns.TODO_PRIORITY, newToDo.getIntPriority());
                appContentResolver.insert(CONTENT_URI, contentValues);
            }
        }
    }

    public static List<ToDo> getToDoListFromDB() {
        List<ToDo> returnList = new ArrayList<>();
        String[] projection = {
                Columns.TODO_ID,
                Columns.TODO_NAME,
                Columns.TODO_DESCRIPTION,
                Columns.TODO_DEADLINE,
                Columns.TODO_PRIORITY
        };
        Cursor cursor = appContentResolver.query(CONTENT_URI,
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
                    deadline = new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex(Columns.TODO_DEADLINE)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int priority = cursor.getInt(cursor.getColumnIndex(Columns.TODO_PRIORITY));
                returnList.add(new ToDo(id, name, description, deadline, priority));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return returnList;
    }

    public static void deleteToDoListFromDB(List<Integer> listToDelete) {
        for(Integer i : listToDelete) {
            appContentResolver.delete(buildToDoUri(i), null, null);
        }
    }

    private static ToDo isExistsByID(List<ToDo> list, ToDo toDo) {
        for(ToDo existingToDo : list) {
            if (existingToDo.getId() == toDo.getId()) return existingToDo;
        }
        return null;
    }

    public static List<ToDo> getNotExpiredToDos() {
        List<ToDo> currentToDoList = getToDoListFromDB();
        List<ToDo> notExpiredList = new ArrayList<>();
        for (ToDo toDo : currentToDoList) {
            if (!toDo.isExpired()) {
                notExpiredList.add(toDo);
            }
        }
        return notExpiredList;
    }
}
