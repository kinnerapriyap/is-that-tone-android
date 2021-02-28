package com.kinnerapriyap.sugar

import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate

object Destinations {
    const val Home = "home"
    const val GameCard = "gameCard"
    const val RoundOver = "roundOver"
    const val GameOver = "gameOver"
    const val WordCard = "wordCard"

    object WordCardArgs {}
}

class Actions(navController: NavHostController) {
    val returnHome: () -> Unit = {
        navController.navigate(Destinations.Home)
    }
    val openGameCard: () -> Unit = {
        navController.navigate(Destinations.GameCard)
    }
    val showRoundOver: () -> Unit = {
        navController.navigate(Destinations.RoundOver)
    }
    val showGameOver: () -> Unit = {
        navController.navigate(Destinations.GameOver)
    }
    val openWordCard: () -> Unit = {
        navController.navigate(Destinations.WordCard)
    }
    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }
}