package com.example.dreamjournal

import android.content.Context
import com.example.dreamjournal.data.AppDatabase
import com.example.dreamjournal.ml.RuleBasedSentimentAnalyzer
import com.example.dreamjournal.repo.DreamRepository
import com.example.dreamjournal.security.CryptoUtil

object ServiceLocator {
    @Volatile private var db: AppDatabase? = null
    @Volatile private var repo: DreamRepository? = null

    fun repository(context: Context): DreamRepository = repo ?: synchronized(this) {
        repo ?: DreamRepository(database(context).dreamDao()).also { repo = it }
    }

    fun analyzer() = RuleBasedSentimentAnalyzer()

    fun crypto(context: Context) = CryptoUtil(context)

    private fun database(context: Context): AppDatabase = db ?: synchronized(this) {
        db ?: AppDatabase.instance(context).also { db = it }
    }
}
