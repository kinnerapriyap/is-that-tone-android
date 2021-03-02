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
import com.kinnerapriyap.sugar.ui.screens.GameOverScreen
import com.kinnerapriyap.sugar.ui.screens.HomeScreen
import com.kinnerapriyap.sugar.ui.screens.WordCardScreen

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
                    actions.showGameOver
                )
            }
            composable(Destinations.GameOver) {
                GameOverScreen(viewModel, actions.returnHome)
            }
            composable(Destinations.WordCard) {
                WordCardScreen(viewModel, actions.navigateBack)
            }
        }
    }
}
