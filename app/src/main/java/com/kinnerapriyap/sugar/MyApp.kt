package com.kinnerapriyap.sugar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kinnerapriyap.sugar.ui.screens.GameCardScreen
import com.kinnerapriyap.sugar.ui.screens.HomeScreen
import com.kinnerapriyap.sugar.ui.screens.WordCardScreen
import com.kinnerapriyap.sugar.ui.screens.RoundOverScreen
import com.kinnerapriyap.sugar.ui.screens.GameOverScreen

@ExperimentalFoundationApi
@Composable
fun MyApp(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }
    Surface(color = MaterialTheme.colors.background) {
        NavHost(navController = navController, startDestination = Destinations.Home) {
            composable(Destinations.Home) {
                HomeScreen(viewModel, actions.openGameCard)
            }
            composable(Destinations.GameCard) {
                GameCardScreen(
                    viewModel,
                    actions.openWordCard,
                    actions.showRoundOver,
                    actions.showGameOver
                )
            }
            composable(Destinations.RoundOver) {
                RoundOverScreen(actions.openGameCard, actions.navigateBack)
            }
            composable(Destinations.GameOver) {
                GameOverScreen(actions.returnHome)
            }
            composable(Destinations.WordCard) {
                WordCardScreen()
            }
        }
    }
}