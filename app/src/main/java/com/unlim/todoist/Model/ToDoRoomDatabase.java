package com.unlim.todoist.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ToDo.class}, version = 2, exportSchema = false)
public abstract class ToDoRoomDatabase extends RoomDatabase {
    public abstract ToDoDao toDoDao();

    private static volatile ToDoRoomDatabase INSTANCE;

    static ToDoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ToDoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ToDoRoomDatabase.class, "tododb")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
