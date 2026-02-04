package com.multiplayer.local

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.multiplayer.local.model.SeData
import com.multiplayer.local.model.Type
import com.multiplayer.local.screen.MultiplayerApp
import com.multiplayer.local.utils.Aes
import com.multiplayer.local.utils.Preferences
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(
            WindowInsetsCompat.Type.statusBars() or
                    WindowInsetsCompat.Type.navigationBars()
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

//        val gameView = GameView(this)
//        setContentView(gameView)

//        hostBtn.setOnClickListener {
//            LanServer.start()
//            LanDiscovery.startBroadcast()
//        }
//
//        joinBtn.setOnClickListener {
//            LanDiscovery.listen { hostIp ->
//                LanClient.connect(hostIp)
//            }
//        }
        Log.e(TAG, "onCreadddddddte: ${Preferences(this).isTimeNotEnded()}")
        if(Preferences(this).isTimeNotEnded()){
            val currentTime = System.currentTimeMillis()
            Preferences(this).saveAdMobAdsShown(0)
            Preferences(this).saveUnityAdsShown(0)
            Preferences(this).saveCurrentTime(currentTime.toString())
        }

        setContent {
            MultiplayerApp()
        }
//        setContentView(layout)
    }
}

fun getAll(context: Context, dataFetched: (SeData) -> Unit) {
    Thread {
        var jsonServerString: String = ""
        try {
            val serverUrl = URL("https://git1234game.github.io/aii/tfdData")
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(serverUrl)
                .addHeader(
                    "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                            "Chrome/120.0.0.0 Safari/537.36"
                )
                .addHeader("Referer", "https://google.com")
                .addHeader("Accept", "application/json")
                .addHeader("Connection", "keep-alive")
                .build()
            client.newCall(request).execute().use { response ->
                response.body?.let {
                    jsonServerString = it.string()
                    val cleanedData = jsonServerString.replace("\n", "")
                    Preferences(context).saveAdData(cleanedData)
                    val type: Type = Type()
                    val decryptData = Aes.decryptObject(cleanedData)
                    val seData: SeData = Gson().fromJson(decryptData, type.di())
                    dataFetched.invoke(seData)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "getCoinData: ${e.message}")
        }
    }.start()
}
