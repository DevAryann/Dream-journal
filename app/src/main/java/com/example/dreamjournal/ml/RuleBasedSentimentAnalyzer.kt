package com.example.dreamjournal.ml

import com.example.dreamjournal.domain.SentimentAnalyzer
import kotlin.math.max
import kotlin.math.min

/**
 * Tiny rule-based sentiment/mood classifier with a small lexicon.
 * You can later replace this with a TFLite model or cloud NLP.
 */
class RuleBasedSentimentAnalyzer : SentimentAnalyzer {
    private val positive = setOf(
        "happy", "joy", "great", "good", "love", "calm", "peace", "excited", "hope",
        "bright", "fun", "beautiful", "pleasant", "relief", "relieved"
    )
    private val negative = setOf(
        "sad", "fear", "scared", "anxious", "anxiety", "angry", "mad", "hate", "bad",
        "dark", "cry", "nightmare", "stress", "stressed", "pain", "hurt", "lonely"
    )

    override fun analyze(text: String): SentimentAnalyzer.Result {
        val tokens = text.lowercase().split(" ", "\n", "\t", ",", ".", "!", "?", ";", ":")
            .filter { it.isNotBlank() }
        var score = 0
        for (t in tokens) {
            when (t) {
                in positive -> score += 1
                in negative -> score -= 1
            }
        }
        // Normalize score to -1..1 using tanh-like squashing, but simple clamp given tiny lexicon
        val normalized = when {
            score > 0 -> min(1f, score / 5f)
            score < 0 -> max(-1f, score / 5f)
            else -> 0f
        }
        val label = when {
            normalized > 0.2 -> "Positive"
            normalized < -0.2 -> "Negative"
            else -> "Neutral"
        }
        return SentimentAnalyzer.Result(label, normalized)
    }
}
