package com.multiplayer.local.utils

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.util.Log
import com.multiplayer.local.utils.LanServer.disconnectAllClients
import com.multiplayer.local.utils.LanServer.job
import com.multiplayer.local.utils.LanServer.server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.Collections


@Volatile
var randomPosition = HostRandomPosition()
private const val PORT = 8000

@Volatile
var players: MutableList<PlayerState> = mutableListOf()

@Volatile
var isStart: Boolean = false

@Volatile
var isBreakLoop: Boolean = false

@Volatile
var isTrueGameOver: Boolean = false

/* ---------------------------------------------------
   NETWORK UTILS
--------------------------------------------------- */
object NetworkUtils {
    fun getGatewayIp(context: Context): String {
        val wifi = context.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        val dhcp = wifi.dhcpInfo
        return Formatter.formatIpAddress(dhcp.gateway)
    }
}

/* ---------------------------------------------------
   HOST (PHONE THAT CREATES HOTSPOT)
--------------------------------------------------- */
var id = 0

object LanServer {
    private var server: ServerSocket? = null
    private var job: Job? = null
    private val clients = Collections.synchronizedList(mutableListOf<Socket>())


    fun stop() {
        id = 0
        isStart = false
        isTrueGameOver = false
        players.clear()
        GameState.localPlayer = PlayerState()
        players = mutableListOf()

        Log.d("LAN", "daaaaaaa $players")
        try {
            job?.cancel()          // stop coroutine
            job = null
            disconnectAllClients()
            server?.close()
            server = null
//            while (true) {
//                if (server?.isClosed == true ) {
//                    onConnected.invoke()
//                    break
//                }
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun disconnectAllClients() {
        synchronized(clients) {
            clients.forEach {
                try {
                    it.close()
                } catch (_: Exception) {}
            }
            clients.clear()
        }
    }

    fun start(
        playerName: String,
        backPress: Boolean = false,
        numberOfPlayer: Int = 10,
        onConnected: (MutableList<PlayerState>) -> Unit
    ) {
        isBreakLoop = false
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
            server = ServerSocket(PORT)
            while (server != null) {
                    if (backPress) {
                        id = 0
                        isStart = false
                        players.clear()
                        Log.d("LAN", "daaaaaaa $players")
                        server!!.close()
                    } else {

                        players.add(PlayerState(id = id, player = playerName))
                        GameState.localPlayer = GameState.localPlayer.copy(
                            player = playerName, id = id
                        )
                        val list: MutableList<PlayerState> = mutableListOf()
                        list.add(PlayerState(id = id, player = playerName))
                        while (players.size < numberOfPlayer) {
                            if (isStart) {
                                Log.d("LAN", "SERVER STARTED1111")
                                server!!.close()
                                break
                            }
                            id++
                            Log.d("LAN", "SERVER STARTED2222")
                            val client = server!!.accept()
                            clients.add(client)
                            val input = BufferedReader(InputStreamReader(client.getInputStream()))
                            val output = PrintWriter(client.getOutputStream(), true)
                            if (!isStart) {
                                // Send host name to client
                                output.println("$playerName, $id")

                                // Receive client name
                                val clientName = input.readLine()
                                players.add(PlayerState(id = id, player = clientName))
                                list.add(PlayerState(id = id, player = clientName))
                                onConnected(list)

                                GameSync.handleConnection(input, output, isHost = true)
//                            }
                            }
                        }
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        job?.start()
    }
}

/* ---------------------------------------------------
   CLIENT (PHONE THAT JOINS HOTSPOT)
--------------------------------------------------- */
object LanClient {

    private var socket: Socket? = null
    private var job: Job? = null

    fun stop() {
        id = 0
        isStart = false
        isTrueGameOver = false
        players.clear()
        GameState.localPlayer = PlayerState()
        players = mutableListOf()
        try {
            job?.cancel()          // stop coroutine
            job = null
            disconnectAllClients()
            socket?.close()
            socket = null
//            while (true) {
//                if (server?.isClosed == true ) {
//                    onConnected.invoke()
//                    break
//                }
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun connect(
        context: Context,
        playerName: String,
        onConnected: (String) -> Unit
    ) {
        isBreakLoop = false
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val hostIp = NetworkUtils.getGatewayIp(context)

                socket = Socket(hostIp, PORT)

                val input = BufferedReader(InputStreamReader(socket?.getInputStream()))
                val output = PrintWriter(socket?.getOutputStream(), true)

                // Receive host name
                val data = input.readLine()
                val parts = data.split(",")
                // Send my name
                output.println(playerName)

                val ids = parts[1].trim().toInt()
                GameState.localPlayer = GameState.localPlayer.copy(
                    id = ids, player = playerName
                )
                onConnected(parts[0])

                GameSync.handleConnection(input, output, isHost = false)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        job?.start()
    }
}

/* ---------------------------------------------------
   GAME SYNC (REAL-TIME INPUT SHARING)
--------------------------------------------------- */
object GameSync {

    fun handleConnection(
        input: BufferedReader,
        output: PrintWriter,
        isHost: Boolean
    ) {

        // SEND LOCAL INPUT
        Thread {
            try {
                while (true) {
                    if (isBreakLoop){
                        break
                    }
                    if (isHost) {
                        val inputState = playersToString()
                        output.println(inputState)
                    } else {
                        val inputState = getLocalInput()
                        output.println(inputState)
                    }
                    if (isHost) {
                        val random = hostRandom()
                        output.println(random)
                    }
                    Thread.sleep(16) // ~60 FPS
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()

        // RECEIVE REMOTE INPUT
        Thread {
            try {
                while (true) {
                    if (isBreakLoop){
                        break
                    }
                    val remoteInput = input.readLine() ?: break
                    if (isHost) {
                        applyRemoteInput(remoteInput)
                    } else {
                        applyPlayer(remoteInput)
                    }
                    if (!isHost) {
                        val remoteInput = input.readLine() ?: break
                        applyRandom(remoteInput)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun getLocalInput(): String {
        // Example format
        val p = GameState.localPlayer
        return "${p.x},${p.y},${p.score},${p.life},${p.isStart},${p.player},${p.id},${p.isGameOver}"
    }

    //    is host player send
    fun playersToString(): String {
        val index = players.indexOfFirst { it.id == GameState.localPlayer.id }
        if (index != -1) {
            players[index] = GameState.localPlayer.copy(
                isStart = isStart
            )
        }
        return players.joinToString("|") { p ->
            "${p.x},${p.y},${p.score},${p.life},${p.isStart},${p.player},${p.id},${p.isGameOver}"
        }
    }

    //    is host apply get player
    fun applyPlayer(data: String) {
        val listData: MutableList<PlayerState> = mutableListOf()
        val listPlayer = data.trim().split("|")
        if (listPlayer.size > 1) {
            for (element in listPlayer) {
                val parts = element.split(",")
                if (parts.size == 8) {
                    val x = parts[0].toFloat()
                    val y = parts[1].toFloat()
                    val score = parts[2].trim().toInt()
                    val life = parts[3].trim().toInt()
                    val isStart = parts[4].toBoolean()
                    val player = parts[5]
                    val id = parts[6].trim().toInt()
                    val gameOver = parts[7].toBoolean()
                    listData.add(PlayerState(x, y, score, life, isStart, player, id, gameOver))
                }
            }
        }
        players = listData
        Log.e("ddddff", "applyPlayerfddfdd: $listData", )
        var alivePlayers = listData.size
        for (player in listData) {
            if (player.isGameOver) {
                alivePlayers--
            } else {
                alivePlayers++
            }
            if (alivePlayers == 0) {
                isTrueGameOver = true
            }
        }
    }

    private fun hostRandom(): String {
        // Example format
        val p = randomPosition
        return "${p.random1},${p.random2},${p.random3},${p.random4},${p.random5}, ${p.random6},${p.random7}"
    }

    fun applyRandom(data: String) {
        val parts = data.split(",")
        if (parts.size == 7) {
            val random1 = parts[0].toFloat()
            val random2 = parts[1].toFloat()
            val random3 = parts[2].toFloat()
            val random4 = parts[3].toFloat()
            val random5 = parts[4].toFloat()
            val random6 = parts[5].toFloat()
            val random7 = parts[6].toFloat()
            randomPosition =
                HostRandomPosition(random1, random2, random3, random4, random5, random6, random7)
        }
    }

    fun applyRemoteInput(data: String) {
        val parts = data.split(",")
        if (parts.size == 8) {
            val x = parts[0].toFloat()
            val y = parts[1].toFloat()
            val score = parts[2].toInt()
            val life = parts[3].toInt()
            val isStart = parts[4].toBoolean()
            val player = parts[5]
            val id = parts[6].toInt()
            val gameOver = parts[7].toBoolean()

            val playerState = players.find {
                it.id == id && it.player == player
            }
            Log.e("dfddffd", "applyRemoteInput: $data")
            if (playerState != null && playerState.id != GameState.localPlayer.id &&
                playerState.player != GameState.localPlayer.player
            ) {
                playerState.x = x
                playerState.y = y
                playerState.score = score
                playerState.life = life
                playerState.isStart = isStart
                playerState.isGameOver = gameOver
            }

            var alivePlayers = players.size
            for (player in players) {
                if (player.isGameOver) {
                    alivePlayers--
                } else {
                    alivePlayers++
                }
                if (alivePlayers == 0) {
                    isTrueGameOver = true
                }
            }
//            else {
//                listData.add(
//                    PlayerState(
//                        id = id,
//                        player = player,
//                        x = x,
//                        y = y,
//                        score = score,
//                        life = life,
//                        isStart = isStart
//                    )
//                )
//            }

        }
    }
}

data class PlayerState(
    var x: Float = 0f,
    var y: Float = 0f,
    var score: Int = 0,
    var life: Int = 2,
    var isStart: Boolean = false,
    var player: String = "",
    var id: Int = 0,
    var isGameOver: Boolean = false,
)

data class HostRandomPosition(
    var random1: Float = 0f,
    var random2: Float = 0f,
    var random3: Float = 0f,
    var random4: Float = 0f,
    var random5: Float = 0f,
    var random6: Float = 0f,
    var random7: Float = 0f,
    val random8: Float = 0f,
)

object GameState {
    @Volatile
    var localPlayer = PlayerState()
}




