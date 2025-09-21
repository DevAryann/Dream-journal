package com.example.dreamjournal.domain

interface SentimentAnalyzer {
    data class Result(
        val mood: String, // Positive, Negative, Neutral
        val score: Float // -1.0..1.0
    )
    fun analyze(text: String): Result
}
