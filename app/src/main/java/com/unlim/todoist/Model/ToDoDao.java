package com.unlim.todoist.Model;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface ToDoDao {
    @Query("SELECT * FROM ToDo")
    List<ToDo> getAll();

    @Query("SELECT * FROM ToDo WHERE _id = :id")
    ToDo getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ToDo todo);

    @Delete
    int delete(ToDo todo);
}
