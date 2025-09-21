package com.example.dreamjournal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dreams")
data class Dream(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val title: String,
    val content: String,
    val mood: String, // e.g., Positive/Negative/Neutral or custom label
    val moodScore: Float, // -1..1
    val tags: List<String> = emptyList()
)
