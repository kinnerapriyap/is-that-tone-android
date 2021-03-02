/*
 * Copyright 2021 Kinnera Priya Putti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kinnerapriyap.sugar

import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import androidx.navigation.compose.popUpTo

object Destinations {
    const val Home = "home"
    const val GameCard = "gameCard"
    const val GameOver = "gameOver"
    const val WordCard = "wordCard"
}

class Actions(navController: NavHostController) {
    val returnHome: () -> Unit = {
        navController.navigate(Destinations.Home) {
            popUpTo(Destinations.Home) {
                inclusive = true
            }
        }
    }
    val openGameCard: () -> Unit = {
        navController.navigate(Destinations.GameCard)
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
