package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameSaveDao {
    @Query("SELECT * FROM game_saves ORDER BY slotId ASC")
    fun getAllSaves(): Flow<List<GameSaveEntity>>

    @Query("SELECT * FROM game_saves WHERE slotId = :slotId LIMIT 1")
    suspend fun getSaveBySlotId(slotId: Int): GameSaveEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSave(save: GameSaveEntity)

    @Query("DELETE FROM game_saves WHERE slotId = :slotId")
    suspend fun deleteSaveBySlotId(slotId: Int)
}
