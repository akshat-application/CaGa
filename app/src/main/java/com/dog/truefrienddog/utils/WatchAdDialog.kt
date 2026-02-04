package com.dog.truefrienddog.utils

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dog.truefrienddog.R
import com.dog.truefrienddog.ui.theme.dialogCardBackground
import com.dog.truefrienddog.ui.theme.dialogMiningBackground
import com.dog.truefrienddog.ui.theme.speedBackground


@Composable
fun WatchAdDialog(
    title: String,
    description: String,
    positive: String,
    negative: String,
    onPositiveClicked: () -> Unit,
    onUseCoinClick: () -> Unit,
    onNegativeClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = dialogMiningBackground)
            .clickable(enabled = false) {}
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp),
            colors = CardDefaults.cardColors(containerColor = dialogCardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Cancel,
                contentDescription = "cancel icon",
                tint = Color.Black,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        onNegativeClicked.invoke()
                    }
                    .align(Alignment.End)
                    .padding(top = 8.dp, end = 8.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .clickable { }) {
                    Button(
                        onClick = {
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = speedBackground
                        ),
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .DrawBorder(
                                shape = RoundedCornerShape(100),
                                durationMillis = 10000,
                                stroke = 4.dp
                            )
                            .align(Alignment.Center)
                    ) {

                    }
                    Icon(
                        imageVector = Icons.Outlined.RocketLaunch,
                        contentDescription = "Error Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = description,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (!positive.equals(AppConstant.REMOVE_BUTTON)) {
                        Button(
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .DrawBorder(
                                    shape = RoundedCornerShape(4),
                                    durationMillis = 10000,
                                    stroke = 2.dp
                                ),
                            onClick = {
                                onPositiveClicked.invoke()
                            }
                        ) {
                            Text(text = positive, color = Color.White)
                        }
                    }
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (!positive.equals(AppConstant.REMOVE_BUTTON)) {
                        Button(
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(top = 8.dp)
                                .DrawBorder(
                                    shape = RoundedCornerShape(4),
                                    durationMillis = 10000,
                                    stroke = 2.dp
                                ),
                            onClick = {
                                onUseCoinClick.invoke()
//                                onPositiveClicked.invoke()
                            }
                        ) {
                            val context = LocalContext.current
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = AppConstant.USE_COIN, color = Color.White)
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
                        }
                    }
                }
            }
        }
    }
}


