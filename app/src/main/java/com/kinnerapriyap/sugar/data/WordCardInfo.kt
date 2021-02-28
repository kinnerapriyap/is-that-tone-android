package com.kinnerapriyap.sugar.data

data class WordCardInfo(
    val wordCard: WordCard? = null,
    val usedAnswers: List<String>? = emptyList()
)