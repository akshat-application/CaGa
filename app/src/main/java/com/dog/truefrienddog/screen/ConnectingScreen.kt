package com.dog.truefrienddog.screen

// ---------- IMPORTS ----------
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dog.truefrienddog.utils.AppConstant
import com.dog.truefrienddog.utils.GameState
import com.dog.truefrienddog.utils.GameState.localPlayer
import com.dog.truefrienddog.utils.LanClient
import com.dog.truefrienddog.utils.LanServer
import com.dog.truefrienddog.utils.PlayerState
import com.dog.truefrienddog.utils.isStart
import com.dog.truefrienddog.utils.players
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ---------- COMPOSABLE ----------

val colorList = listOf(
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Yellow,
    Color.Cyan,
    Color.Magenta,
    Color(0xFFB044AF),
    Color(0xFFA477A4),
    Color(0xFF917734),
    Color.White,
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Yellow,
    Color.Cyan,
    Color.Magenta,
)

@Composable
fun StartGameScreen(
    isHost: Boolean = false,
    player: String = "Fighter 1",
    onStartClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var player2 by remember {
        mutableStateOf<List<PlayerState>>(
            listOf()
        )
    }
    var name by remember { mutableStateOf("") }
    var isRefresh by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    if (isHost) {
            LaunchedEffect(Unit) {
                LanServer.start(player) {
                    coroutineScope.launch {
                        isRefresh = true
                        delay(1000)
                        isRefresh = false
                    }
            }
        }
    } else {
        LaunchedEffect(Unit) {
            LanClient.connect(context, player) {
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            player2 = players
            Log.e("TAG", "StartGameScreen: $player2")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // Center panel
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = AppConstant.WINING_CONDITION,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.DarkGray)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = AppConstant.WINING_NOTE,
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    placeholder = {
                        Text(AppConstant.HINT_WINING)
                    },
                    modifier = Modifier.width(240.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Title
                Text(
                    text = "START GAME",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.DarkGray)
                )

                Spacer(modifier = Modifier.height(16.dp))
                if (isRefresh) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 160.dp)
                    ) {
                        items(player2, itemContent = { playerData ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Player ${playerData.id + 1}:",
                                    color = colorList[playerData.id],
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = playerData.player,
                                    color = Color.White,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        })
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 160.dp)
                    ) {
                        items(player2, itemContent = { playerData ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Player ${playerData.id + 1}:",
                                    color = colorList[playerData.id],
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = playerData.player,
                                    color = Color.White,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        })
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                // START Button
                if (isHost) {
                    Button(
                        onClick = {
                            if(players.size > 1) {
                                isStart = true
                                onStartClick.invoke()
                            } else {
                                Toast.makeText(context, AppConstant.WAIT_OTHER_PEOPLE, Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            disabledContainerColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(2.dp)
                    ) {
                        Text(
                            text = "START",
                            color = Color.LightGray,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LaunchedEffect(Unit) {
                        while (true) {
                            delay(16)
                            val remote = if (players.size > 1) players[0] else localPlayer
                            if (remote.isStart) {
                                onStartClick.invoke()
                                break
                            }
                        }
                    }
                }
                Text(
                    text = AppConstant.BACK,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            onBackClick.invoke()
                            LanServer.stop()
                        }
                        .padding(16.dp)
                )

            }
        }

    }
}