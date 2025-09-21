package com.example.dreamjournal.repo

import com.example.dreamjournal.data.Dream
import com.example.dreamjournal.data.DreamDao
import kotlinx.coroutines.flow.Flow

class DreamRepository(private val dao: DreamDao) {
    fun dreams(): Flow<List<Dream>> = dao.observeAll()
    fun dream(id: Long): Flow<Dream?> = dao.observeById(id)

    suspend fun save(dream: Dream): Long = dao.upsert(dream)
    suspend fun delete(dream: Dream) = dao.delete(dream)
}
