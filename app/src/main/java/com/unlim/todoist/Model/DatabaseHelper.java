package com.unlim.todoist.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "tododb.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Database.TODO_TABLE_NAME + "(" +
                Database.Columns.TODO_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                Database.Columns.TODO_NAME + " TEXT NOT NULL, " +
                Database.Columns.TODO_DESCRIPTION + " TEXT, " +
                Database.Columns.TODO_DEADLINE + " TEXT NOT NULL, " +
                Database.Columns.TODO_PRIORITY + " INTEGER NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
