package com.dog.truefrienddog.screen.coinScreen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dog.truefrienddog.MainActivity
import com.dog.truefrienddog.R
import com.dog.truefrienddog.screen.GameScreenViewModel
import com.dog.truefrienddog.screen.initFirstTime
import com.dog.truefrienddog.ui.theme.backGroundDashboard
import com.dog.truefrienddog.ui.theme.backgroundPopularColor
import com.dog.truefrienddog.ui.theme.newButtonStyle
import com.dog.truefrienddog.ui.theme.newButtonStyle1
import com.dog.truefrienddog.ui.theme.popularBoxTextStyle
import com.dog.truefrienddog.ui.theme.textColor
import com.dog.truefrienddog.utils.AdsLoadDialog
import com.dog.truefrienddog.utils.AppConstant
import com.dog.truefrienddog.utils.DrawBorder
import com.dog.truefrienddog.utils.Preferences

@Composable
fun CoinScreen(viewModel: GameScreenViewModel = hiltViewModel(), onBack: () -> Unit) {
    val context = LocalContext.current
    var isReferBoxVisible by remember { mutableStateOf(true) }
    var titleData by remember { mutableStateOf(Preferences(context).getTitleData()) }
    viewModel.title_data.collectAsState().value.let {
        titleData = it
    }
    var loading by remember { mutableStateOf(false) }
    viewModel.isLoading.collectAsState().value.let {
        loading = it
    }
//    viewModel.isLoginSuccess.collectAsState().value.let {
//        isReferBoxVisible = it
//    }

//    val coinData = Preferences(context).getCoinData()
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
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(backGroundDashboard)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    modifier = Modifier.padding(20.dp),
                    backgroundColor = backgroundPopularColor,
                    elevation = 12.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(backgroundPopularColor)
                            .padding(8.dp)
                            .clickable {
                            }

                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .height(height = 120.dp)
                                    .fillMaxWidth()
                            ) {
                                Row() {
                                    Column(
                                        modifier = Modifier
                                            .width(200.dp)
                                            .align(Alignment.CenterVertically)
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(end = 8.dp),
                                            text = AppConstant.BALANCE,
                                            style = popularBoxTextStyle,
                                        )
                                        Column {
                                            Row {
                                                Text(
                                                    modifier = Modifier.padding(top = 2.dp),
                                                    text = Preferences(context).getCoinData()
                                                        .toString(),
                                                    style = newButtonStyle1
                                                )
                                                AsyncImage(
                                                    model = ImageRequest.Builder(context)
                                                        .data(R.drawable.tfd_logo)
                                                        .allowHardware(false)
                                                        .crossfade(true)
                                                        .build(),
                                                    contentDescription = "User profile pic",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .align(Alignment.CenterVertically)
                                                        .padding(start = 2.dp)
                                                        .size(28.dp)
                                                )
                                            }
                                            if (titleData.isBa) {
                                                Text(
                                                    modifier = Modifier.padding(top = 2.dp),
                                                    text = titleData.wallData,
                                                    style = newButtonStyle
                                                )
                                            }
                                        }
                                    }
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(R.drawable.account_balance_wallet_24)
                                            .allowHardware(false)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "User profile pic",
                                        contentScale = ContentScale.Crop,
                                        colorFilter = ColorFilter.tint(
                                            textColor
                                        ),
                                        modifier = Modifier
                                            .size(80.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                    backgroundColor = backgroundPopularColor,
                    elevation = 12.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(backgroundPopularColor)
                            .padding(8.dp)
                            .clickable {

                            }

                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .fillMaxWidth()
                            ) {
                                Row() {
                                    Column(
                                        modifier = Modifier
                                            .width(200.dp)
                                            .align(Alignment.CenterVertically)
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(end = 8.dp),
                                            text = AppConstant.WHAT_IS_TFD,
                                            style = popularBoxTextStyle,
                                        )
                                        Text(
                                            modifier = Modifier.padding(top = 2.dp),
                                            text = AppConstant.TFD_DESCRIPTION,
                                            style = newButtonStyle
                                        )
                                    }
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(R.drawable.tfd_logo)
                                            .allowHardware(false)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "User profile pic",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .padding(start = 2.dp)
                                            .align(Alignment.CenterVertically)
                                            .size(80.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                        .DrawBorder(
                            shape = RoundedCornerShape(1),
                            durationMillis = 40000,
                            stroke = 4.dp
                        )
                        .clickable {
                            viewModel.showAds(context as Activity) {
                                val balance = Preferences(context).getCoinData() + 1
                                Preferences(context).saveCoinData(balance)
                            }
                        },
//                    backgroundColor = backgroundPopularColor,
//                    elevation = 12.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(backgroundPopularColor)
                            .padding(8.dp)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .fillMaxWidth()
                            ) {
                                Row() {
                                    Column(
                                        modifier = Modifier
                                            .width(200.dp)
                                            .align(Alignment.CenterVertically)
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(end = 8.dp),
                                            text = AppConstant.WATCH_AD,
                                            style = popularBoxTextStyle,
                                        )
                                        val modifiedText = titleData.supDis.replace(
                                            AppConstant.COPY_ADDRESS,
                                            "",
                                            ignoreCase = true
                                        )
                                        Text(
                                            modifier = Modifier.padding(
                                                top = 2.dp,
                                                bottom = 2.dp
                                            ),
                                            text = AppConstant.TAP_TO_WATCH,
                                            style = newButtonStyle
                                        )
                                        Text(
                                            modifier = Modifier.padding(
                                                top = 2.dp,
                                                bottom = 2.dp
                                            ),
                                            text = titleData.Add,
                                            style = newButtonStyle
                                        )
//                                            AddressDetails(titleData.Add)
                                    }
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(R.drawable.baseline_card_giftcard_24)
                                            .allowHardware(false)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "User profile pic",
                                        contentScale = ContentScale.Crop,
                                        colorFilter = ColorFilter.tint(
                                            textColor
                                        ),
                                        modifier = Modifier
                                            .padding(start = 2.dp)
                                            .align(Alignment.CenterVertically)
                                            .size(80.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (loading){
        AdsLoadDialog()
    }
}