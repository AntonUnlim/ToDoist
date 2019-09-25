package com.unlim.todoist.Model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ToDoProvider extends ContentProvider {
    private DatabaseHelper dbOpenHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static final int TODO_ALL = 100;
    public static final int TODO_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(Database.AUTHORITY, Database.TODO_TABLE_NAME, TODO_ALL);
        matcher.addURI(Database.AUTHORITY, Database.TODO_TABLE_NAME + "/#", TODO_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionAgrs, String sortOrder) {
        final int match = uriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (match) {
            case TODO_ALL:
                queryBuilder.setTables(Database.TODO_TABLE_NAME);
                break;
            case TODO_ID:
                queryBuilder.setTables(Database.TODO_TABLE_NAME);
                long toDoID = Database.getToDoID(uri);
                queryBuilder.appendWhere(Database.Columns.TODO_ID + "=" + toDoID);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        return queryBuilder.query(db, projection, selection, selectionAgrs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch(match){
            case TODO_ALL:
                return Database.CONTENT_TYPE;
            case TODO_ID:
                return Database.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = uriMatcher.match(uri);
        final SQLiteDatabase db;
        Uri returnUri;
        long recordId;

        switch (match) {
            case TODO_ALL:
                db = dbOpenHelper.getWritableDatabase();
                recordId = db.insert(Database.TODO_TABLE_NAME, null, contentValues);
                if (recordId > 0) {
                    returnUri = Database.buildToDoUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert: " + uri.toString());
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        final SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String selectionCriteria = selection;

        if (match != TODO_ALL && match != TODO_ID) {
            throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        if (match == TODO_ID) {
            long toDoID = Database.getToDoID(uri);
            selectionCriteria = Database.Columns.TODO_ID + "=" + toDoID;
            if ((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
            }
        }
        return db.delete(Database.TODO_TABLE_NAME, selectionCriteria, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        final SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String selectionCriteria = selection;
        if (match != TODO_ALL && match != TODO_ID) {
            throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        if (match == TODO_ID) {
            long toDoID = Database.getToDoID(uri);
            selectionCriteria = Database.Columns.TODO_ID + "=" + toDoID;
            if ((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
            }
        }
        return db.update(Database.TODO_TABLE_NAME, contentValues, selectionCriteria, selectionArgs);
    }
}
