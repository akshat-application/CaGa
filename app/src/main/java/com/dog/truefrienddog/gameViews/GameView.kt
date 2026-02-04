package com.multiplayer.local.gameViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toArgb
import com.multiplayer.local.screen.colorList
import com.multiplayer.local.utils.GameState
import com.multiplayer.local.utils.players
import com.multiplayer.local.utils.randomPosition
import kotlin.random.Random


class GameView(
    context: Context,
    isMultiplePlayer: Boolean = true,
    var isHost: Boolean = false,
    val life: Int = 2,
    val onGameOver: (Int) -> Unit
) : View(context) {

    private val paintLocal = Paint().apply { color = Color.BLUE }
    private val paintRemote = Paint().apply { color = Color.RED }

//    val gameOver : () -> Unit = onGameOver

    private val isMultiPlayer = isMultiplePlayer

    // smoothing
    private var smoothRemoteX = 0f
    private var smoothRemoteY = 0f

    val WORLD_WIDTH = 1000f
    val PLAYER_SIZE_WORLD = 80f
    val WORLD_HEIGHT = 600f

    var yAxisChange = 0f

    private val player1 = RectF()
    private val player2 = RectF()

    private val playerWidth = 100f
    private val playerHeight = 100f

    private var p2X = 300f
    private var p2Y = 0f

    private var p1X = 300f
    private var p1Y = 0f
    private val margin = 40f

    private val paint = Paint()
    private val treats = mutableListOf<Zombie>()

    //    Zombie(100f, 300f, 80f)
    private var score = 0
    private var gameOver = false
    private var lives = life
    private val gameLoop = object : Runnable {
        override fun run() {
            update()
            invalidate()
            postDelayed(this, 16)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        p1X = margin
        p1Y = h - playerHeight - margin

        // Player 2 → RIGHT
        p2X = w - playerWidth - margin
        p2Y = h - playerHeight - margin

        player1.set(
            p1X,
            p1Y,
            p1X + playerWidth,
            p1Y + playerHeight
        )
        if (isMultiPlayer) {
            player2.set(
                p2X,
                p2Y,
                p2X + playerWidth,
                p2Y + playerHeight
            )
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private val spawnRunnable = object : Runnable {
        override fun run() {
            spawnTreat()
            handler.postDelayed(this, 1000)
        }
    }

    init {
        if (isMultiPlayer) {
            post(gameLoop)
        }
        handler.post(spawnRunnable)
    }

    private fun update() {
        val alpha = 0.2f
        for (element in players) {
            smoothRemoteX += (element.x - smoothRemoteX) * alpha
            smoothRemoteY += (element.y - smoothRemoteY) * alpha
        }
    }

    fun worldToScreenX(x: Float) = (x / WORLD_WIDTH) * width
    fun worldToScreenY(y: Float) = (y / WORLD_HEIGHT) * height
    fun worldToScreenSize(size: Float) = (size / WORLD_WIDTH) * width

    private val zombie = Zombie(100f, 300f, 80f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)
        val colorLoacal = dogColorSkins[GameState.localPlayer.id]

        if (GameState.localPlayer.life > 0) {
            DogDrawer.drawDog(canvas,player1.centerX(), player1.centerY(), bodyPaint = colorLoacal.body ,
                earPaint = colorLoacal.ear, eyePaint = colorLoacal.eye, nosePaint = colorLoacal.nose  )
//            canvas.drawRect(player1, colorLoacal)
        } else {
//            p1X = -10_000f
//            p1Y = -10_000f
//
//            player1.set(
//                p1X,
//                p1Y,
//                p1X + playerWidth,
//                p1Y + playerHeight
//            )
        }
        if (isMultiPlayer) {
            val remotes = players
            val remoteSize = worldToScreenSize(PLAYER_SIZE_WORLD)
            remotes.forEachIndexed { index, remote ->
                val color = dogColorSkins[index]
                if (remote.id != GameState.localPlayer.id) {
                    if (remote.x == 0f || remote.y == 0f) {
                        DogDrawer.drawDog(canvas,player2.centerX(), player2.centerY(), bodyPaint = color.body ,
                            earPaint = color.ear, eyePaint = color.eye, nosePaint = color.nose  )
//                        canvas.drawRect(player2, color)
                    } else {
                        if (remote.life > 0) {
                            val rect = RectF(worldToScreenX(remote.x),
                                worldToScreenY(remote.y),
                                worldToScreenX(remote.x) + remoteSize,
                                worldToScreenY(remote.y) + remoteSize)
                            DogDrawer.drawDog(canvas,rect.centerX(), rect.centerY(), bodyPaint = color.body ,
                                earPaint = color.ear, eyePaint = color.eye, nosePaint = color.nose  )
                        } else {
                            p1X = -10_000f
                            p1Y = -10_000f
                            canvas.drawRect(
                                p1X,
                                p1Y,
                                p1X + playerWidth,
                                p1Y + playerHeight,
                                paintRemote
                            )
                        }
                    }
                }
            }
        }
        paint.color = Color.RED
        val iterator = treats.iterator()
        while (iterator.hasNext()) {
            val treat = iterator.next()
//            treat.top += 60
//            treat.bottom += 60

            if (RectF.intersects(player1, treat.rect)) {
                GameState.localPlayer.life--
//                lives--
                iterator.remove()
            } else if (treat.top() > height ) {
                if (GameState.localPlayer.life > 0) {
                    score++
                }
                iterator.remove()
            }
            treat.update()
            drawZombie(canvas, treat)
//            invalidate()
        }
        postInvalidateOnAnimation()
        paint.color = Color.WHITE
        paint.textSize = 48f
        canvas.drawText("Score: $score", 40f, 100f, paint)
        canvas.drawText("${GameState.localPlayer.life} :Lives", width - 200f, 100f, paint)
        if (GameState.localPlayer.life <= 0) {
//            handler.removeCallbacks(spawnRunnable)
//            handler.removeCallbacks(gameLoop)
            onGameOver.invoke(score)
            gameOver = true
//            lives++

//            paint.textSize = 120f
//            paint.textAlign = Paint.Align.CENTER
//            val x = width / 2f
//            val y = (height / 2f) - ((paint.descent() + paint.ascent()) / 2)
//            canvas.drawText("GAME OVER", x, y, paint)
            if (!isMultiPlayer) {
                return
            }
        } else {
            gameOver = false
        }
        invalidate()
    }

    private fun spawnTreat() {
        if (GameState.localPlayer.life <= 0 && !isMultiPlayer) return
        val size = worldToScreenSize(80f)

        if (isMultiPlayer) {
            if (isHost) {
                randomPosition.apply {
                    val x = Random.nextInt(0, width - size.toInt()).toFloat()
                    random1 = x
                    treats.add(Zombie(worldToScreenX(x), worldToScreenY(0f), size))
                    if (score > 4) {
                        val y = Random.nextInt(0, width - size.toInt()).toFloat()
                        random2 = y
                        treats.add(Zombie(worldToScreenX(y), worldToScreenY(0f), size, 20f))
                    }
                    if (score > 20) {
                        val z = Random.nextInt(0, width - size.toInt()).toFloat()
                        random3 = z
                        treats.add(Zombie(worldToScreenX(z), worldToScreenY(0f), size, 25f))
                    }
                    if (score > 50) {
                        val a = Random.nextInt(0, width - size.toInt()).toFloat()
                        random4 = a
                        treats.add(Zombie(worldToScreenX(a), worldToScreenY(0f), size, 30f))
                    }
                    if (score > 100) {
                        val b = Random.nextInt(0, width - size.toInt()).toFloat()
                        random5 = b
                        treats.add(Zombie(worldToScreenX(b), worldToScreenY(0f), size, 35f))
                    }
                    if (score > 300) {
                        val c = Random.nextInt(0, width - size.toInt()).toFloat()
                        random6 = c
                        treats.add(Zombie(worldToScreenX(c), worldToScreenY(0f), size, 40f))
                    }
                    if (score > 700) {
                        val d = Random.nextInt(0, width - size.toInt()).toFloat()
                        random7 = d
                        treats.add(Zombie(worldToScreenX(d), worldToScreenY(0f), size, 40f))
                    }
                    Log.e("score", "spawnTreat: $random1")
                }
            } else {
                val x = randomPosition.random1
                treats.add(Zombie(worldToScreenX(x), 0f, size))
                if (score > 4) {
                    val y = randomPosition.random2
                    treats.add(Zombie(worldToScreenX(y), worldToScreenY(0f), size, 20f))
                }
                if (score > 20) {
                    val z = randomPosition.random3
                    treats.add(Zombie(worldToScreenX(z), worldToScreenY(0f), size, 25f))
                }
                if (score > 50) {
                    val a = randomPosition.random4
                    treats.add(Zombie(worldToScreenX(a), worldToScreenY(0f), size, 30f))
                }
                if (score > 100) {
                    val b = randomPosition.random5
                    treats.add(Zombie(worldToScreenX(b), worldToScreenY(0f), size, 35f))
                }
                if (score > 300) {
                    val c = randomPosition.random6
                    treats.add(Zombie(worldToScreenX(c), worldToScreenY(0f), size, 40f))
                }
                if (score > 700) {
                    val d = randomPosition.random7
                    treats.add(Zombie(worldToScreenX(d), worldToScreenY(0f), size, 40f))
                }
                Log.e("score1", "spawnTreat: ${randomPosition.random1}")
            }
        } else {
            val x = Random.nextInt(0, width - size.toInt()).toFloat()
            Log.e("score2", "spawnTreat: ${randomPosition.random1}")
            treats.add(Zombie(x, 0f, size))
            if (score > 4) {
                val y = Random.nextInt(0, width - size.toInt()).toFloat()
                treats.add(Zombie(y, 0f, size, 20f))
            }
            if (score > 20) {
                val z = Random.nextInt(0, width - size.toInt()).toFloat()
                treats.add(Zombie(z, 0f, size, 25f))
            }
            if (score > 50) {
                val a = Random.nextInt(0, width - size.toInt()).toFloat()
                treats.add(Zombie(a, 0f, size, 30f))
            }
            if (score > 100) {
                val b = Random.nextInt(0, width - size.toInt()).toFloat()
                treats.add(Zombie(b, 0f, size, 35f))
            }
            if (score > 300) {
                val c = Random.nextInt(0, width - size.toInt()).toFloat()
                treats.add(Zombie(c, 0f, size, 40f))
            }
            if (score > 700) {
                val d = Random.nextInt(0, width - size.toInt()).toFloat()
                treats.add(Zombie(d, 0f, size, 40f))
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
            val worldX = (event.x / width) * WORLD_WIDTH
            val worldY = (event.y / height) * WORLD_HEIGHT
            val localSize = worldToScreenSize(PLAYER_SIZE_WORLD)
            if (GameState.localPlayer.life > 0) {
                player1.set(
                    worldToScreenX(worldX),
                    worldToScreenY(worldY + yAxisChange),
                    worldToScreenX(worldX) + localSize,
                    worldToScreenY(worldY + yAxisChange) + localSize
                )
            }
            GameState.localPlayer = GameState.localPlayer.copy(
                x = worldX,
                y = worldY,
                score = score,
                life = GameState.localPlayer.life,
                isStart = false,
                isGameOver = gameOver
            )
        }
        return true
    }
}

class Zombie(
    var x: Float,
    var y: Float,
    var size: Float,
    var speed: Float = 15f
) {
    var legAngle = 0f
    var direction = 1

    val rect = RectF()

    fun update() {
        // Move zombie forward
        y += speed

        // Leg animation
        legAngle += 2f * direction
        if (legAngle > 20 || legAngle < -20) {
            direction *= -1
        }
        updateRect()
    }

    fun top(): Float {
        return y - size / 3
    }

    private fun updateRect() {
        rect.set(
            x - size / 3,
            y - size / 3,
            x + size / 3,
            y + size + size / 2
        )
    }
}

fun drawZombie(canvas: Canvas, zombie: Zombie) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Body
    paint.color = Color.rgb(90, 160, 90)
    canvas.drawRect(
        zombie.x - zombie.size / 3,
        zombie.y,
        zombie.x + zombie.size / 3,
        zombie.y + zombie.size,
        paint
    )

    // Head
    canvas.drawCircle(
        zombie.x,
        zombie.y - zombie.size / 3,
        zombie.size / 3,
        paint
    )

    // Legs (walking)
    paint.strokeWidth = 8f
    paint.color = Color.GRAY

    canvas.drawLine(
        zombie.x - zombie.size / 6,
        zombie.y + zombie.size,
        zombie.x - zombie.size / 6 + zombie.legAngle,
        zombie.y + zombie.size + zombie.size / 2,
        paint
    )

    canvas.drawLine(
        zombie.x + zombie.size / 6,
        zombie.y + zombie.size,
        zombie.x + zombie.size / 6 - zombie.legAngle,
        zombie.y + zombie.size + zombie.size / 2,
        paint
    )
}


