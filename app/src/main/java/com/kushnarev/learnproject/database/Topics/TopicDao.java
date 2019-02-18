package com.kushnarev.learnproject.database.Topics;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TopicDao {

    @Query("SELECT * FROM topic")
    List<TopicEntry> loadAllTopics();

    @Insert
    void insertTopic(TopicEntry topicEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
//    @Update
    void updateTopic(TopicEntry topicEntry);

    @Delete
    void deleteTopic(TopicEntry topicEntry);

    @Query("SELECT * FROM topic WHERE id = :id")
    TopicEntry loadTopicById(int id);

    @Query("SELECT * FROM topic WHERE course_id=:courseId")
    List<TopicEntry> findTopicsForCourse(final int courseId);
}
