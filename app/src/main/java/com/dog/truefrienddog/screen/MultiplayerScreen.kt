package com.multiplayer.local.screen

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplayer.local.model.SeData
import com.multiplayer.local.utils.Aes
import com.multiplayer.local.utils.AppConstant
import com.google.gson.Gson


@Composable
fun MultiplayerScreen(
    onHost: () -> Unit,
    onJoin: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        // Main panel
        Column(
            modifier = Modifier
                .width(380.dp)
                .border(1.dp, Color.Gray)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title
            Text(
                text = AppConstant.MULTI_PLAYER,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(color = Color.Gray, thickness = 1.dp)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = AppConstant.NOTE,
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Buttons row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                // CREATE GAME
                RetroButton(
                    labelTop = AppConstant.WIFI_CREATED_PLAYER,
                    labelMain = AppConstant.HOST_GAME,
                    topColor = Color.Cyan,
                    onClick = onHost
                )

                Spacer(modifier = Modifier.width(12.dp))

                // JOIN GAME
                RetroButton(
                    labelTop = AppConstant.OTHER_PLAYER,
                    labelMain = AppConstant.JOIN_GAME,
                    topColor = Color.Red,
                    onClick = onJoin
                )
            }

            Text(
                text = AppConstant.BACK,
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable { onBack() }
            )
        }
    }
}

@Composable
fun RetroButton(
    labelTop: String,
    labelMain: String,
    topColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .border(1.dp, Color.Gray)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = labelTop,
            color = topColor,
            fontSize = 10.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = labelMain,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
    }
}

