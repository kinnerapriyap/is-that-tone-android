package com.kinnerapriyap.sugar.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kinnerapriyap.sugar.MainViewModel
import com.kinnerapriyap.sugar.R
import com.kinnerapriyap.sugar.data.GameCardInfo
import com.kinnerapriyap.sugar.ui.screens.CardState.Companion.getCardState

enum class CardState {
    DONE, CURRENT, TO_PLAY;

    companion object {
        fun getCardState(roundNo: String, activeRound: Int?) =
            when {
                roundNo < activeRound.toString() -> DONE
                roundNo == activeRound.toString() -> CURRENT
                else -> TO_PLAY
            }
    }
}

@ExperimentalFoundationApi
@Composable
fun GameCardScreen(
    viewModel: MainViewModel = viewModel(),
    openWordCard: () -> Unit,
    showGameOver: () -> Unit
) {
    val gameCardInfo: GameCardInfo by viewModel.gameCardInfo.observeAsState(GameCardInfo())
    Scaffold {
        if (!gameCardInfo.isStarted) {
            StartDimOverlay(
                isActivePlayer = gameCardInfo.isActivePlayer,
                startGame = { viewModel.startGame(openWordCard) }
            )
        }
        LazyVerticalGrid(cells = GridCells.Fixed(2)) {
            items(gameCardInfo.answers.toList()) { (roundNo, answer) ->
                val cardState = getCardState(roundNo, gameCardInfo.activeRound)
                Card(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(40.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(
                            role = Role.Button,
                            enabled = cardState == CardState.CURRENT && answer.isNullOrBlank()
                        ) {
                            openWordCard.invoke()
                        },
                    backgroundColor = when (cardState) {
                        CardState.DONE -> Color.LightGray
                        CardState.CURRENT -> MaterialTheme.colors.primary
                        CardState.TO_PLAY -> MaterialTheme.colors.secondary
                    },
                    elevation = 4.dp,
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
fun StartDimOverlay(
    isActivePlayer: Boolean,
    startGame: () -> Unit
) {
    DimOverlay(alpha = 0x66) {
        if (isActivePlayer) {
            Button(onClick = { startGame.invoke() }) {
                Text(text = stringResource(id = R.string.start_game))
            }
        } else {
            Text(text = stringResource(id = R.string.waiting_to_start))
        }
    }
}

@Composable
fun DimOverlay(
    alpha: Int = 0x66,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
            .background(Color(0, 0, 0, alpha)),
        contentAlignment = Alignment.Center,
        content = content
    )
}