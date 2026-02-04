package com.multiplayer.local.screen

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.multiplayer.local.gameViews.GameView
import com.multiplayer.local.getAll
import com.multiplayer.local.model.PlayerInfo
import com.multiplayer.local.model.SeData
import com.multiplayer.local.screen.coinScreen.CoinScreen
import com.multiplayer.local.screen.result.ResulScreen
import com.multiplayer.local.utils.AdsLoadDialog
import com.multiplayer.local.utils.AdsUtils
import com.multiplayer.local.utils.Aes
import com.multiplayer.local.utils.AppConstant
import com.multiplayer.local.utils.GameState
import com.multiplayer.local.utils.LanClient
import com.multiplayer.local.utils.LanServer
import com.multiplayer.local.utils.Preferences
import com.multiplayer.local.utils.WatchAdDialog
import com.multiplayer.local.utils.isBreakLoop
import com.multiplayer.local.utils.isTrueGameOver
import com.multiplayer.local.utils.players
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


var loadFirst: Boolean = true
var initFirstTime: Boolean = true

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MultiplayerApp(viewModel: GameScreenViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var seData by remember { mutableStateOf(Preferences(context).getAdData()) }
    val scope = rememberCoroutineScope()

    if (loadFirst) {
        getAll(context) {
            seData = it
            val encrypt = Aes.encryptObject(Gson().toJson(SeData()))
            Log.d(TAG, "getCoinData: ${encrypt}")
            loadFirst = false
        }
    }
    var loading by remember { mutableStateOf(false) }
    viewModel.isLoading.collectAsState().value.let {
        loading = it
    }

    var openAppLoader by remember {
        mutableStateOf(true)
    }
    if (openAppLoader && seData.isOpen) {
        AdsUtils().loadOpenAppAd(context as Activity) {
            scope.launch {
                delay(1000)
                val updatedData = Preferences(context).getAdData()
                if (updatedData.isOpen) {
                    AdsUtils().showOpenAppAd(context)
                }
            }
        }
        openAppLoader = false
    }
    var player1 by remember { mutableStateOf("player1") }
    var isHost by remember { mutableStateOf(false) }
    var isGameOverDialog by remember { mutableStateOf(false) }
    var isSinglePlayer by remember { mutableStateOf(false) }
    var loadFistTime by remember { mutableStateOf(true) }
    var screen by remember { mutableStateOf<Screen>(Screen.Menu) }
    var life by remember { mutableStateOf(2) }
    val playerInfo = PlayerInfo()
    var gameOverT by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    when (val s = screen) {

        Screen.Menu -> {
            loadFistTime = true
            MainMenu(
                singlePlayer = {
                    isSinglePlayer = true
                    screen = Screen.SinglePlayer
                },
                multiplayer = {
                    isSinglePlayer = false
                    screen = Screen.MultiPlayer
                },
                result = {
                    screen = Screen.HistoryScreen
                },
                coin = {
                    screen = Screen.CoinScreen
                }
            )
        }

        Screen.CoinScreen -> {
            CoinScreen(viewModel) {
                screen = Screen.Menu
            }
        }

        Screen.SinglePlayer -> {
            AndroidView(
                factory = {
                    GameView(it, isMultiplePlayer = false, life = life) {
                        if (loadFistTime) {
                            isGameOverDialog = true
                            loadFistTime = false
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Screen.MultiPlayer -> MultiplayerScreen(
            onHost = {
                isHost = true
                screen = Screen.NameCreate
            },
            onJoin = {
                isHost = false
                screen = Screen.NameCreate
            },
            onBack = {
                screen = Screen.Menu
            }
        )

        Screen.NameCreate -> PlayerNameScreen(
            isHost = isHost,
            {
                screen = Screen.MultiPlayer
            },
            {
                if (isHost) {
                    player1 = it
                    screen = Screen.HostWaiting
                } else {
                    player1 = it
                    screen = Screen.HostWaiting
                }
            }
        )

        Screen.HistoryScreen -> {
            ResulScreen() {
                screen = Screen.Menu
            }
        }

        Screen.HostWaiting -> StartGameScreen(isHost, player1, {
            if (isHost) {
                screen = Screen.Game
            } else {
                screen = Screen.Game
            }
        }, {
            screen = Screen.NameCreate
        })


        is Screen.Game -> {
            AndroidView(
                factory = {
                    GameView(it, isHost = isHost, life = life) {
                        if (loadFistTime) {
//                        playerName = GameState.localPlayer.player
                            playerInfo.score = GameState.localPlayer.score.toString()
                            isGameOverDialog = true
                            loadFistTime = false
                        }
//                        if(!isSinglePlayer){
                        if (players.size > 1 && isTrueGameOver) {
//                                if (players.size > 1 && players.all { it.isGameOver }) {
                            val currentDateTime = LocalDateTime.now()
                            val formatter =
                                DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
                            playerInfo.apply {
                                playerName =
                                    GameState.localPlayer.player.toString()
                                winCondition
                                score = GameState.localPlayer.score.toString()
                                dateTime = currentDateTime.format(formatter)
                                val playersScore =
                                    mutableListOf<Pair<String, String>>()
                                for (element in players) {
                                    playersScore.add(
                                        Pair(
                                            element.player,
                                            element.score.toString()
                                        )
                                    )
                                }
                                allPlayerScore = playersScore
                                yourRank
                                isMultiPlayer = false
                            }
                            gameOverT = true
                            Log.e(TAG, "MultiplayerApp: ${playerInfo}")
                            if (isHost) {
                                LanServer.stop()
                            } else {
                                LanClient.stop()
                            }
                            isBreakLoop = true
                            Preferences(context).saveMultiPlayerHistory(
                                playerInfo
                            )
                            screen = Screen.HistoryScreen
//                                }
                        }
//                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    fun gameOverAll() {

    }

    if (isGameOverDialog) {
        WatchAdDialog(
            AppConstant.GAME_OVER,
            AppConstant.BOOST_DESCRIPTION,
            AppConstant.WATCH_AD,
            "",
            {
                GameState.localPlayer = GameState.localPlayer.copy(
                    isGameOver = false
                )
                viewModel.showAds(context as Activity, true) {
                    GameState.localPlayer = GameState.localPlayer.copy(
                        life = 1
                    )
                    life = 1
                    if (isSinglePlayer) {
                        screen = Screen.SinglePlayer
                    } else {
                        screen = Screen.Game
                    }
                    loadFistTime = true
                    isGameOverDialog = false
                }
            },
            onUseCoinClick = {

                val coin = Preferences(context).getCoinData()
                if (coin > 0.99) {
                    Preferences(context).saveCoinData(coin - 1).let {
                        GameState.localPlayer = GameState.localPlayer.copy(
                            isGameOver = false,
                            life = 1
                        )
                        life = 1
                        if (isSinglePlayer) {
                            screen = Screen.SinglePlayer
                        } else {
                            screen = Screen.Game
                        }
                        loadFistTime = true
                        isGameOverDialog = false
                    }
                } else {
                    Toast.makeText(context, AppConstant.ZERO_BALANCE_NOTE, Toast.LENGTH_SHORT)
                        .show()
                }
            },
            onNegativeClicked = {
//                loadFistTime = true
                if (isSinglePlayer) {
                    val currentDateTime = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
                    playerInfo.dateTime = currentDateTime.format(formatter)
                    playerInfo.score = GameState.localPlayer.score.toString()
                    playerInfo.isMultiPlayer = false
                    Log.e(TAG, "MultiplayerApp: ${playerInfo}")

                    Preferences(context).saveSinglePlayerHistory(playerInfo)
                    GameState.localPlayer = GameState.localPlayer.copy(
                        life = 2,
                        score = 0,
                    )
                    screen = Screen.Menu
                } else {
                    if (gameOverT) {
                        screen = Screen.HistoryScreen
                    } else {
                        screen = Screen.Game
                    }
                }
                isGameOverDialog = false
            })
//        AIDisclaimerScreen() {
//            isGameOverDialog = false
//            screen = Screen.Menu
//        }
    }
    if (loading) {
        AdsLoadDialog()
    }
}

sealed class Screen {
    object Menu : Screen()
    object MultiPlayer : Screen()
    object SinglePlayer : Screen()
    object NameCreate : Screen()
    object HostWaiting : Screen()
    object CoinScreen : Screen()
    object HistoryScreen : Screen()
    object Game : Screen()
}

@Composable
fun MainMenu(
    singlePlayer: () -> Unit, multiplayer: () -> Unit,
    result: () -> Unit, coin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = singlePlayer,
            modifier = Modifier.width(200.dp)
        ) {
            Text(AppConstant.SINGLE_PLAYER)
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = multiplayer,
            modifier = Modifier.width(200.dp)
        ) {
            Text(AppConstant.MULTI_PLAYER)
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = result,
            modifier = Modifier.width(200.dp)
        ) {
            Text(AppConstant.RESULT_HISTORY)
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = coin, modifier = Modifier.width(200.dp)) {
            Text(AppConstant.COLLECT_COIN)
        }
    }
}



