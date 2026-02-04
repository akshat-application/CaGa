package com.multiplayer.local.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplayer.local.utils.AppConstant


@Composable
fun PlayerNameScreen(
    isHost: Boolean = false,
    onBack: () -> Unit,
    onCreate: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        /* MAIN CARD */
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Gray)
                    .padding(24.dp)
                    .width(320.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = if (isHost) AppConstant.HOST_PLAYER_NAME else AppConstant.JOIN_PLAYER_NAME,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Name:",
                            color = Color.White,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            singleLine = true,
                            modifier = Modifier.width(180.dp),
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

                    Spacer(modifier = Modifier.height(32.dp))

                    /* CREATE BUTTON */
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Gray)
                            .clickable {
                                if (name.isNotEmpty()) {
                                    onCreate(name)
                                } else {
                                    Toast.makeText(
                                        context,
                                        AppConstant.ENTER_NAME,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .padding(horizontal = 32.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = if (isHost) AppConstant.CREATE else AppConstant.JOIN,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            textAlign = TextAlign.Center
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

    }
}
