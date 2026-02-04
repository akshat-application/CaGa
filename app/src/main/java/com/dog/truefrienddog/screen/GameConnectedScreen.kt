package com.multiplayer.local.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiplayer.local.utils.GameState
import com.multiplayer.local.utils.PlayerState

@Composable
fun GameConnectedScreen(host: String, join: String) {
//    var smoothRemote by mutableStateOf(PlayerState())

//    LaunchedEffect(GameState.remotePlayer) {
////        val target = GameState.remotePlayer
////        val current = smoothRemote
////
////        smoothRemote = PlayerState(
////            x = current.x + (target.x - current.x) * 0.2f,
////            y = current.y + (target.y - current.y) * 0.2f
////        )
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game Connected 🎮", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(24.dp))

        Text("Host: ${host}")
        Text("Player: ${join}")
    }
}