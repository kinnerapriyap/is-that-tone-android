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
package com.kinnerapriyap.sugar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kinnerapriyap.sugar.MainViewModel
import com.kinnerapriyap.sugar.R

@Composable
fun GameOverScreen(
    viewModel: MainViewModel = viewModel(),
    returnHome: () -> Unit
) {
    val scores by viewModel.scores.observeAsState(emptyMap())
    Scaffold {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                items(scores.toList()) { (player, score) ->
                    Text(text = "${player.name} : $score", modifier = Modifier.padding(4.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { returnHome.invoke() }) {
                Text(text = stringResource(id = R.string.finish))
            }
        }
    }
}
