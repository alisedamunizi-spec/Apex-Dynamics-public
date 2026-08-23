package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_saves")
data class GameSaveEntity(
    @PrimaryKey val slotId: Int, // 1 = AutoSave, 2 = Slot 1, 3 = Slot 2, 4 = Slot 3
    val slotName: String,
    val saveTimestamp: Long = System.currentTimeMillis(),
    val companyName: String,
    val year: Long,
    val eraDisplayName: String,
    val money: Double,
    val valuation: Double,
    val devicesCount: Int,
    val osCount: Int,
    val researchedTechCount: Int,
    val jsonPayload: String
)
