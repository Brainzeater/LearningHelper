package com.kushnarev.learnproject.database.Chunks;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ChunkDao {

    @Query("SELECT * FROM chunk")
    List<ChunkEntry> loadAllChunks();

    @Insert
    void insertChunk(ChunkEntry chunkEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateChunk(ChunkEntry chunkEntry);

    @Delete
    void deleteChunk(ChunkEntry chunkEntry);

    @Query("SELECT * FROM chunk WHERE id = :id")
    ChunkEntry loadChunkById(int id);

    @Query("SELECT * FROM chunk WHERE topic_id=:topicId")
    List<ChunkEntry> findChunksForTopic(final int topicId);
}
