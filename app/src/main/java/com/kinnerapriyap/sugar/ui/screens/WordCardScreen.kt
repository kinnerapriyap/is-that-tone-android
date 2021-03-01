package com.kinnerapriyap.sugar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kinnerapriyap.sugar.MainViewModel
import com.kinnerapriyap.sugar.R
import com.kinnerapriyap.sugar.data.WordCardInfo

@Composable
fun WordCardScreen(
    viewModel: MainViewModel = viewModel(),
    navigateBack: () -> Unit
) {
    val wordCardInfo: WordCardInfo by viewModel.wordCardInfo.observeAsState(WordCardInfo())
    var selectedAnswer by rememberSaveable { mutableStateOf<String?>(null) }
    Scaffold {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Word: ${wordCardInfo.wordCard?.word ?: ""}")
            LazyColumn {
                items(
                    wordCardInfo.wordCard?.answers?.toList() ?: return@LazyColumn
                ) { (answerChar, answer) ->
                    Button(
                        onClick = { selectedAnswer = answerChar },
                        enabled = wordCardInfo.usedAnswers?.contains(answerChar) == false
                    ) {
                        Text(text = "$answerChar : $answer")
                    }
                }
            }
            Button(
                onClick = {
                    selectedAnswer?.let { answer ->
                        viewModel.setAnswer(answer, navigateBack)
                    }
                },
                enabled = selectedAnswer?.isNotBlank() == true,
            ) {
                Text(text = stringResource(id = R.string.submit_option))
            }
        }
    }
}