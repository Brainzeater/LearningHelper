package com.kushnarev.learnproject.database.Courses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "course")
public class CourseEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;

    @Ignore
    public CourseEntry(String title) {
        this.title = title;
    }

    public CourseEntry(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
