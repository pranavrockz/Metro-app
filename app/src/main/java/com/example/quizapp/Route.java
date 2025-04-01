package com.example.quizapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Route_table")
//Entity is a fundamental component that represents a table in the Sqlite database.Each entity class corresponds
//to one table and the fields properties or the variables within the entity class represents columns in that table
//Entity define the structure and the schema of your database table
public class Route {
    @ColumnInfo(name="route_id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="Station_name")
    private String name;
    @ColumnInfo(name="Line_Color")
    private String Line;

    @ColumnInfo(name="Previous_Station")
    private String prev;

    @ColumnInfo(name="distance_previous_station")
    private float dist_prev;
    @ColumnInfo(name="Next_Station")
    private String next;

    @ColumnInfo(name="distance_next_station")
    private float dist_next;

    public Route(String name, String line, String prev, float dist_prev, String next, float dist_next) {
        this.name = name;
        this.Line = line;
        this.prev = prev;
        this.dist_prev = dist_prev;
        this.next = next;
        this.dist_next = dist_next;
    }

    //This is used to prevent any null pointer exceptions.
    public Route(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine() {
        return Line;
    }

    public void setLine(String line) {
        Line = line;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public float getDist_prev() {
        return dist_prev;
    }

    public void setDist_prev(float dist_prev) {
        this.dist_prev = dist_prev;
    }

    public float getDist_next() {
        return dist_next;
    }

    public void setDist_next(float dist_next) {
        this.dist_next = dist_next;
    }
}
