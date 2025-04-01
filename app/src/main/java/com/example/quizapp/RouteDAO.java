package com.example.quizapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RouteDAO {
    @Insert
    void insert(Route route);
    @Delete
    void delete(Route route);
    @Query("Select * from Route_table")
    //By returning data as live data the room libarary ensures that the data is observed by the repository or View Model alloeing for
    //real time updates when the underlying data changes
    LiveData<List<Route>> getAllRoutes();
}
