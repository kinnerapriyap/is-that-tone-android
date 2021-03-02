package com.kinnerapriyap.sugar.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
            modifier = Modifier.padding(20.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Word: ${wordCardInfo.wordCard?.word ?: ""}")
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                items(
                    wordCardInfo.wordCard?.answers?.toList() ?: return@LazyColumn
                ) { (answerChar, answer) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .padding(4.dp)
                            .clickable { selectedAnswer = answerChar },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            modifier = Modifier.padding(8.dp),
                            checked = selectedAnswer == answerChar &&
                                    wordCardInfo.usedAnswers?.contains(answerChar) == false,
                            onCheckedChange = null,
                            enabled = wordCardInfo.usedAnswers?.contains(answerChar) == false
                        )
                        Text(text = answer, modifier = Modifier.padding(8.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    selectedAnswer?.let { answer ->
                        viewModel.setAnswer(answer, navigateBack)
                    }
                },
                enabled = selectedAnswer?.isNotBlank() == true,
            ) {
                Text(text = stringResource(id = R.string.confirm))
            }
        }
    }
}