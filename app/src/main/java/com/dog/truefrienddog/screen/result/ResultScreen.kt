package com.dog.truefrienddog.screen.result

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dog.truefrienddog.model.PlayerInfo
import com.dog.truefrienddog.ui.theme.backGroundDashboard
import com.dog.truefrienddog.utils.AppConstant
import com.dog.truefrienddog.utils.DrawBorder
import com.dog.truefrienddog.utils.Preferences

@Composable
fun ResulScreen(onBack: () -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val viewModel: List<PlayerInfo>
    val tabs = listOf(AppConstant.MULTI_PLAYER,AppConstant.SINGLE_PLAYER)
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val singlePlayerList = Preferences(context).getSinglePlayerHistory()
    val multiPlayerList = Preferences(context).getMultiPlayerHistory()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .DrawBorder(
                shape = RoundedCornerShape(1),
                durationMillis = 40000,
                stroke = 4.dp
            )
            .background(backGroundDashboard)
    ) {
        Scaffold(
            modifier = Modifier
                .background(backGroundDashboard)
                .statusBarsPadding()
                .background(Color.White),
            topBar = {
                TopAppBar(
                    title = { Text("", color = Color.White) },
                    backgroundColor = Color.Black,
                    navigationIcon = {
                        IconButton(onClick = { onBack.invoke() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = AppConstant.BACK,
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    backgroundColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color.Black,
                            height = 4.dp
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }
                when (selectedTabIndex) {
                    0 -> MultiPlayerTab(multiPlayerList) {

                    }
                    1 -> SinglePlayerTab(singlePlayerList) {

                    }
                }
            }
        }
    }
}

@Composable
fun SinglePlayerTab(userSavedData: List<PlayerInfo>, clickOnItem: () -> Unit) {
    if (userSavedData.isNotEmpty()) {
        if (userSavedData.first().score != "0") {
            LazyColumn {
                items(userSavedData.reversed(), itemContent = { item ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "",
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(28.dp)
                        )
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.TopStart)
                                .clickable {
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                            ) {
                                Text(AppConstant.DATE_TIME)
                                Text(item.dateTime)
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Text(AppConstant.YOUR_SCORE)
                                Text(item.score)
                            }
                        }
                        Text(
                            text = item.rank,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(28.dp)
                        )
                    }
                })
            }
        } else {
            Text("No Data Found")
        }
    } else {
        Text("No Data Found")
    }
}

@Composable
fun MultiPlayerTab(userSavedData: List<PlayerInfo>, clickOnItem: () -> Unit) {
    if (userSavedData.isNotEmpty()) {
        if (userSavedData.first().score != "0") {
            LazyColumn {
                items(userSavedData.reversed(), itemContent = { item ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.TopStart)
                                .clickable {
                                }
                        ) {
                            Row() {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp),
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp),
                                    )
                                    {
                                        Text(AppConstant.DATE_TIME)
                                        Text(item.dateTime)
                                    }
                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp)
                                    ) {
                                        Text(AppConstant.YOUR_SCORE)
                                        Text(item.score)
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(16.dp),
                                ) {
                                    Text(AppConstant.ALL_PLAYERS_SCORE)

                                    item.allPlayerScore.reversed().forEach { item ->
                                        Row() {
                                            Text(item.first, modifier = Modifier
                                                .weight(1f))
                                            Text(item.second, modifier = Modifier
                                                .weight(1f))
                                        }
                                    }
                                }
                            }
//                            Text(item.playerName)
//                            Text(item.score)
//                            Text(item.winCondition)
                        }
                        Text(
                            text = item.rank,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(28.dp)
                        )
                    }
                })
            }
        } else {
            Text("No Data Found")
        }
    } else {
        Text("No Data Found")
    }
}