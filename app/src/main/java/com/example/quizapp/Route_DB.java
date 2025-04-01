package com.example.quizapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={Route.class},version=1)
public abstract class Route_DB extends RoomDatabase {
    public abstract RouteDAO getRouteDAO();

    //Singleton Design Pattern in Java to provide a single instance during the life cycle of the app
    // To optimise the usage and prevent creating Multiple Copies or instances of the database.
    private static Route_DB DB_Instance;
    //Synchronized is used to prevent any cleaning of the Route Database.
    //This Singleton Pattern helps maintain consistency,thread safety and efficient resource utilization when
    //working with room database .
    public static synchronized Route_DB getInstance(Context context){
        if(DB_Instance==null){
            //This is a factory method provided by  a room library to create a new database or access
            //an existing one . It allows to configure and build a room database instance
            DB_Instance= Room.databaseBuilder(context.getApplicationContext(),Route_DB.class,
                    "Route_db").fallbackToDestructiveMigration().build();
            //It means that if a new version of the database schema is detected due to changes in the entity structure
            //room will drop and recreate te databse effectively discarding all existing data.This is called destructive migartion
            //and is useful development or when it's acceptable to lose existing data for production apps.
        }
        return DB_Instance;
    }

}
