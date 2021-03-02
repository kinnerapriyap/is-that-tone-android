package com.kinnerapriyap.sugar.data

data class WordCardInfo(
    val wordCard: WordCard? = null,
    val selectedAnswerChar: String? = null,
    val usedAnswers: List<String>? = emptyList(),
    val allowChange: Boolean = true
)