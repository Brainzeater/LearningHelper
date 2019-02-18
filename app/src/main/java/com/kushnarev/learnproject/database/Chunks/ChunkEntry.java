package com.kushnarev.learnproject.database.Chunks;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.kushnarev.learnproject.database.Topics.TopicEntry;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "chunk",
        foreignKeys = @ForeignKey
                (entity = TopicEntry.class,
                        parentColumns = "id",
                        childColumns = "topic_id",
                        onDelete = CASCADE))
public class ChunkEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "topic_id")
    public int topicId;
    @ColumnInfo(name = "chunk_name")
    private String chunkName;

    private String description;

    @Ignore
    public ChunkEntry(String chunkName, int topicId, String description) {
        this.topicId = topicId;
        this.chunkName = chunkName;
        this.description = description;
    }

    public ChunkEntry(int id, String chunkName, int topicId, String description) {
        this.id = id;
        this.topicId = topicId;
        this.chunkName = chunkName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getChunkName() {
        return chunkName;
    }

    public void setChunkName(String chunkName) {
        this.chunkName = chunkName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
