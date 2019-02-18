package com.kushnarev.learnproject.database.Courses;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM course")
    List<CourseEntry> loadAllCourses();

    @Insert
    void insertCourse(CourseEntry courseEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCourse(CourseEntry courseEntry);

    @Delete
    void deleteCourse(CourseEntry courseEntry);

    @Query("SELECT * FROM course WHERE id = :id")
    CourseEntry loadCourseById(int id);
}