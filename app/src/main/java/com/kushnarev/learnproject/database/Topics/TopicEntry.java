package com.kushnarev.learnproject.database.Topics;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.kushnarev.learnproject.database.Courses.CourseEntry;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "topic",
        foreignKeys = @ForeignKey
                (entity = CourseEntry.class,
                        parentColumns = "id",
                        childColumns = "course_id",
                        onDelete = CASCADE))
//                        onUpdate = CASCADE))
public class TopicEntry {
    @PrimaryKey(autoGenerate = true)
//    @PrimaryKey
    public int id;
    @ColumnInfo(name = "course_id")
    public int courseId;
    @ColumnInfo(name = "topic_name")
    private String topicName;
    private long time;
    //    private final int courseId;
    @Ignore
    public TopicEntry(String topicName, int courseId) {
        this.topicName = topicName;
        this.courseId = courseId;
        this.time = 0;
    }

    public TopicEntry(int id, String topicName, int courseId) {
        this.id = id;
        this.topicName = topicName;
        this.courseId = courseId;
        this.time = 0;
    }
//    public TopicEntry(final int id, String topicName, final int courseId) {
//        this.id = id;
//        this.topicName = topicName;
//        this.courseId = courseId;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTopicName() {
        return topicName;
    }
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
