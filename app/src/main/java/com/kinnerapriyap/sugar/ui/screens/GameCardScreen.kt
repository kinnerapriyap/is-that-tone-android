package com.kinnerapriyap.sugar.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kinnerapriyap.sugar.MainViewModel
import com.kinnerapriyap.sugar.R

@ExperimentalFoundationApi
@Composable
fun GameCardScreen(
    viewModel: MainViewModel = viewModel(),
    openWordCard: () -> Unit,
    showRoundOver: () -> Unit,
    showGameOver: () -> Unit
) {
    val rounds by viewModel.rounds.observeAsState(emptyMap())
    val isStarted by viewModel.isStarted.observeAsState(false)
    val isActivePlayer by viewModel.isActivePlayer.observeAsState(false)
    Scaffold {
        if (!isStarted) {
            DimOverlay(
                isActivePlayer = isActivePlayer,
                startGame = { viewModel.startGame(openWordCard) }
            )
        }
        LazyVerticalGrid(cells = GridCells.Fixed(2)) {
            items(rounds.toList()) { (roundNo, answer) ->
                Card(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(40.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            // TODO: Fancy animation to flip card?
                        },
                    border = BorderStroke(2.dp, MaterialTheme.colors.onBackground)
                ) {
                    Text(
                        text = "$roundNo : $answer",
                        modifier = Modifier.wrapContentSize().padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DimOverlay(
    isActivePlayer: Boolean,
    alpha: Int = 0x66,
    startGame: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
            .background(Color(0, 0, 0, alpha)),
        contentAlignment = Alignment.Center
    ) {
        if (isActivePlayer) {
            Button(onClick = { startGame.invoke() }) {
                Text(text = stringResource(id = R.string.start_game))
            }
        } else {
            Text(text = stringResource(id = R.string.waiting_to_start))
        }
    }
}