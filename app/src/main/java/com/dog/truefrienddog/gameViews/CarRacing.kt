package com.dog.truefrienddog.gameViews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import com.multiplayer.local.gameViews.dogColorSkins
import com.multiplayer.local.utils.GameState
import com.multiplayer.local.utils.players
import kotlin.math.sqrt
import kotlin.random.Random


@SuppressLint("ViewConstructor")
class CarRacing(
    context: Context,
    isMultiplePlayer: Boolean = true,
    var isHost: Boolean = false,
    val life: Int = 2,
    val onGameOver: (Int) -> Unit
) : View(context) {
    private var moveLeft = false
    private var moveRight = false
    private var moveUp = false
    private var moveDown = false
    private var boost = false

    private val groundPaint = Paint().apply {
        color = android.graphics.Color.DKGRAY
    }

    private val controlBgPaint = Paint().apply {
        color = android.graphics.Color.BLACK
    }

    private val buttonPaint = Paint().apply {
        color = android.graphics.Color.LTGRAY
    }

    private val boostPaint = Paint().apply {
        color = android.graphics.Color.RED
    }

    private val leftBtn = RectF()
    private val rightBtn = RectF()
    private val upBtn = RectF()
    private val downBtn = RectF()
    private val boostBtn = RectF()

    private val lanePaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 8f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    private val grassPaint = Paint().apply {
        color = Color.rgb(40, 160, 40)
    }

    private var carX = 0f
    private var carY = 0f

    private var speed = 0f
    private var treeSpeed = 0f
    private val NORMAL_SPEED = 8f
    private val BOOST_SPEED = 16f

    private val carWidthRatio = 0.08f   // % of screen width
    private val carHeightRatio = 0.14f  // % of screen height

    private var carRect = RectF()

    private val player1 = RectF()
    private val treats = mutableListOf<Tree>()

    private val isMultiPlayer = isMultiplePlayer
    val WORLD_WIDTH = 1000f
    val WORLD_HEIGHT = 600f

    fun worldToScreenX(x: Float) = (x / WORLD_WIDTH) * width
    fun worldToScreenY(y: Float) = (y / WORLD_HEIGHT) * height
    fun worldToScreenSize(size: Float) = (size / WORLD_WIDTH) * width

    private val handler = Handler(Looper.getMainLooper())

    private val spawnRunnable = object : Runnable {
        override fun run() {
            spawnTreat()
            handler.postDelayed(this, 1000)
        }
    }

    private var smoothRemoteX = 0f
    private var smoothRemoteY = 0f
    private fun update() {
        val alpha = 0.2f
        for (element in players) {
            smoothRemoteX += (element.x - smoothRemoteX) * alpha
            smoothRemoteY += (element.y - smoothRemoteY) * alpha
        }
    }

    val finishY = 100f // Usually a low Y value if the car moves "up"

    val startY = 400f
    val totalTrackLength = startY - finishY

    fun getProgressPercentage(currentY: Float): Int {
        val distanceCovered = startY - currentY
        val progress = (distanceCovered / totalTrackLength) * 100
        return progress.toInt()
    }

    init {
        handler.post(spawnRunnable)
    }

    var roadL = 0
    var roadR = 0
    var screenWidth = 0

    private fun spawnTreat() {
//        if (GameState.localPlayer.life <= 0 && !isMultiPlayer) return
        val size = worldToScreenSize(80f)
        if (screenWidth != 0) {
            val y = Random.nextInt(0, width - size.toInt()).toFloat()
            val randomLeft = Random.nextInt(0, (roadL - 35f).toInt())
            treats.add(Tree(randomLeft.toFloat(), 0f, size, treeSpeed))
            val randomRight = Random.nextInt((roadR + 35f).toInt(), screenWidth.toInt())
            val randomRight1 = Random.nextInt((roadR + 35f).toInt(), screenWidth.toInt())
            treats.add(Tree(randomRight.toFloat(), 0f , size, treeSpeed))
            treats.add(Tree(randomRight1.toFloat(), 0f , size, treeSpeed))
        }
    }

    // Define this in your View or Game Loop class
    private var roadOffset = 0f
    private var backgroundSpeed = 0f // The speed of the road specifically
    private val friction = 0.2f       // How fast it slows down (smaller = slides longer)
    private val acceleration = 200f   // How fast it speeds up
    private val maxBackSpeed = 15f    // Max speed for the background
    private var running =  0f    // Max speed for the background

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var screenW = width.toFloat()
        var screenH = height.toFloat()

        val groundHeight = screenH * 0.7f
        val controlHeight = screenH * 0.3f

        val roadWidth = screenW * 0.55f
        val roadLeft = (screenW - roadWidth) / 2f
        val roadRight = roadLeft + roadWidth

        val laneCount = 5
        val laneWidth = roadWidth / laneCount
        roadR = roadRight.toInt()
        roadL = roadLeft.toInt()
        screenWidth = screenH.toInt()

        if (moveLeft) carX -= speed
        if (moveRight) carX += speed
        if (moveUp) carY -= speed
        if (moveDown) carY += speed
        if (boost) speed = BOOST_SPEED else speed = NORMAL_SPEED

//        roadOffset += speed

// Reset offset when it exceeds the pattern length (100f)
// This prevents the number from getting too large and keeps the loop smooth
        // 1. Calculate the background speed
        val halfHeight = screenH / 2

        if (moveUp && carY <= halfHeight) {
            // Car is in the top half and moving up: Speed up the road
            running += 1
            if (backgroundSpeed < maxBackSpeed) {
                backgroundSpeed += acceleration
            }
        } else {
            // User stopped clicking UP or car is in bottom half: Slow down slowly

            if (backgroundSpeed > 0) {
                backgroundSpeed -= friction
            }
            if (backgroundSpeed < 0) backgroundSpeed = 0f
        }

// 2. Update the road offset using this specific background speed
        roadOffset += backgroundSpeed

// 3. Keep the offset in loop (0 to 100)
        if (roadOffset >= 100f) {
            roadOffset %= 100f
        }

        canvas.drawRect(
            0f,
            0f,
            screenW,
            groundHeight,
            groundPaint
        )

        //// Right grass
        canvas.drawRect(
            roadRight,
            0f,
            screenW,
            groundHeight,
            grassPaint
        )

        for (i in 1 until laneCount) {
            val x = roadLeft + laneWidth * i

            // Start drawing from the offset, but subtract 100 to draw
            // a segment "off-screen" at the top so it slides in smoothly.
            var y = -100f + roadOffset

            while (y < groundHeight) {
                canvas.drawLine(
                    x,
                    y,
                    x,
                    y + 50f, // Length of the dash
                    lanePaint
                )
                y += 100f // Gap between start of one dash and the next
            }
        }
        canvas.drawRect(
            0f,
            0f,
            roadLeft,
            groundHeight,
            grassPaint
        )

        // Subtracting 140 and adding offset makes them scroll smoothly
        var treeY = -140f + (roadOffset % 140f)
        while (treeY < groundHeight) {
            drawTree(canvas, roadRight + 35f, treeY, 0)
            drawTree(canvas, roadRight + 150f, treeY, 1)
            drawTree(canvas, roadRight + 250f, treeY, 2)
            drawTree(canvas, roadLeft - 35f, treeY, 0)
            drawTree(canvas, roadLeft - 150f, treeY, 1)
            drawTree(canvas, roadLeft - 250f, treeY, 2)
            treeY += 140f
        }

        val controlTop = groundHeight

        canvas.drawRect(
            0f,
            controlTop,
            screenW,
            screenH,
            controlBgPaint
        )

        val btnSize = controlHeight * 0.18f
        val centerX = screenW / 2
        val centerY = controlTop + controlHeight / 2

        val boostSize = btnSize * 1.2f
        val spacing = btnSize * 0.6f

        leftBtn.set(
            centerX - btnSize - spacing,
            centerY - btnSize / 2,
            centerX - spacing,
            centerY + btnSize / 2
        )
        canvas.drawRect(leftBtn, buttonPaint)
//
//// RIGHT
        rightBtn.set(
            centerX + spacing,
            centerY - btnSize / 2,
            centerX + btnSize + spacing,
            centerY + btnSize / 2
        )
        canvas.drawRect(rightBtn, buttonPaint)
//
//// UP
        upBtn.set(
            centerX - btnSize / 2,
            centerY - btnSize - spacing,
            centerX + btnSize / 2,
            centerY - spacing
        )
        canvas.drawRect(upBtn, buttonPaint)
//
//// DOWN
        downBtn.set(
            centerX - btnSize / 2,
            centerY + spacing,
            centerX + btnSize / 2,
            centerY + btnSize + spacing
        )
        canvas.drawRect(downBtn, buttonPaint)
//
// BOOST
        val upCenterX = upBtn.centerX()
        val upCenterY = upBtn.centerY()

        val rightCenterX = rightBtn.centerX() + btnSize
        val rightCenterY = rightBtn.centerY()


        val verticalOffset = btnSize * 0.9f   // how much above midpoint
        val boostCenterY = (upCenterY + rightCenterY) / 2f
        val boostCenterX = (upCenterX + rightCenterX + spacing) / 2f

        val finalBoostCenterY = boostCenterY - verticalOffset
        boostBtn.set(
            boostCenterX - boostSize / 2,
            finalBoostCenterY - boostSize / 2,
            boostCenterX + boostSize / 2,
            finalBoostCenterY + boostSize / 2
        )
        canvas.drawRect(boostBtn, boostPaint)
        val colorLoacal = dogColorSkins[GameState.localPlayer.id]

        carX = carX.coerceIn(
            roadLeft,
            roadRight - carRect.width()
        )

        carY = carY.coerceIn(
            groundHeight/2,
            groundHeight - carRect.height()
        )

//        getDistanceToFinish(carX, carY)
        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 50f
        }
        val progress = getProgressPercentage(carY) + running
        val textPaint = Paint().apply {
            color = Color.YELLOW
            textSize = 60f
            isFakeBoldText = true
        }

        canvas.drawText("Progress: $progress%", 50f, 100f, textPaint)

//// Update car rectangle position
//        carRect.set(
//            carX,
//            carY,
//            carX + carRect.width(),
//            carY + carRect.height()
//        )
        drawCarTopView(canvas, carX, carY, 1f,Color.RED, true, true )

//        canvas.drawRoundRect(
//            carRect,
//            20f,
//            20f,
//            carPaint
//        )
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        for (i in 0 until event.pointerCount) {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_POINTER_DOWN -> {
                    val x = event.getX(i)
                    val y = event.getY(i)
                    updateButtonStates(x, y)
                }

                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_POINTER_UP,
                MotionEvent.ACTION_CANCEL -> {
                    resetControls()
                }
            }
        }

        return true
    }

    private fun updateButtonStates(x: Float, y: Float) {
        if (leftBtn.contains(x, y)) moveLeft = true
        if (rightBtn.contains(x, y)) moveRight = true
        if (upBtn.contains(x, y)) moveUp = true
        if (downBtn.contains(x, y)) moveDown = true
        if (boostBtn.contains(x, y)) boost = true
    }

    private var p2X = 300f
    private var p2Y = 0f

    private var p1X = 300f
    private var p1Y = 0f
    private val margin = 40f

    private val playerWidth = 100f
    private val playerHeight = 100f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val screenW = w
        val screenH = h

        val groundHeight = screenH * 0.7f
        val controlHeight = screenH * 0.3f

        val roadWidth = screenW * 0.55f
        val roadLeft = (screenW - roadWidth) / 2f
        val roadRight = roadLeft + roadWidth

        p1X = roadLeft
        p1Y = groundHeight - playerHeight - margin

        // Player 2 → RIGHT
        p2X = roadRight - playerWidth - margin
        p2Y = groundHeight - playerHeight - margin

        carY = groundHeight - playerHeight - margin

        carRect.set(
            p1X,
            p1Y,
            p1X + playerWidth,
            p1Y + playerHeight
        )
    }


    private fun handlePress(x: Float, y: Float) {
        moveLeft = leftBtn.contains(x, y)
        moveRight = rightBtn.contains(x, y)
        moveUp = upBtn.contains(x, y)
        moveDown = downBtn.contains(x, y)
        boost = boostBtn.contains(x, y)
    }

    private fun resetControls() {
        moveLeft = false
        moveRight = false
        moveUp = false
        moveDown = false
        boost = false
    }


}

