package com.dog.truefrienddog.gameViews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import android.graphics.Color
import com.multiplayer.local.gameViews.DogDrawer
import com.multiplayer.local.gameViews.dogColorSkins
import com.multiplayer.local.utils.GameState


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

    private fun drawButton(
        canvas: Canvas,
        x: Float,
        y: Float,
        size: Float,
        paint: Paint
    ) {
        canvas.drawRect(
            x,
            y,
            x + size,
            y + size,
            paint
        )
    }

    private val lanePaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 8f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    private val grassPaint = Paint().apply {
        color = Color.rgb(40, 160, 40)
    }

    private val treeTrunkPaint = Paint().apply {
        color = Color.rgb(120, 80, 50)
    }

    private val treeLeafPaint = Paint().apply {
        color = Color.rgb(30, 140, 30)
        isAntiAlias = true
    }

    private var carX = 0f
    private var carY = 0f

    private var speed = 8f
    private val NORMAL_SPEED = 8f
    private val BOOST_SPEED = 16f

    private val carWidthRatio = 0.08f   // % of screen width
    private val carHeightRatio = 0.14f  // % of screen height

    private var carRect = RectF()

    private val player1 = RectF()


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val screenW = width.toFloat()
        val screenH = height.toFloat()

        val groundHeight = screenH * 0.7f
        val controlHeight = screenH * 0.3f

        val roadWidth = screenW * 0.55f
        val roadLeft = (screenW - roadWidth) / 2f
        val roadRight = roadLeft + roadWidth

        val laneCount = 5
        val laneWidth = roadWidth / laneCount

        if (moveLeft) carX -= speed
        if (moveRight) carX += speed
        if (moveUp) carY -= speed
        if (moveDown) carY += speed
        if (boost) speed = BOOST_SPEED else speed = NORMAL_SPEED

        canvas.drawRect(
            0f,
            0f,
            screenW,
            groundHeight,
            groundPaint
        )

        for (i in 1 until laneCount) {
            val x = roadLeft + laneWidth * i
            var y = 0f

            while (y < groundHeight) {
                canvas.drawLine(
                    x,
                    y,
                    x,
                    y + 50f,
                    lanePaint
                )
                y += 100f
            }
        }
        canvas.drawRect(
            0f,
            0f,
            roadLeft,
            groundHeight,
            grassPaint
        )
//
//// Right grass
        canvas.drawRect(
            roadRight,
            0f,
            screenW,
            groundHeight,
            grassPaint
        )

        fun drawTree(x: Float, y: Float) {
            // trunk
            canvas.drawRect(
                x - 6f,
                y,
                x + 6f,
                y + 35f,
                treeTrunkPaint
            )
//
//            // leaves
            canvas.drawCircle(
                x,
                y,
                20f,
                treeLeafPaint
            )
        }

        var treeY = 60f
        while (treeY < groundHeight) {

            // Left side
            drawTree(roadLeft - 35f, treeY)

            // Right side
            drawTree(roadRight + 35f, treeY)

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
            0f,
            groundHeight - carRect.height()
        )

// Car paint (BEST: define this once outside onDraw)
        val carPaint = Paint().apply {
            color = android.graphics.Color.RED
            isAntiAlias = true
        }

//// Update car rectangle position
        carRect.set(
            carX,
            carY,
            carX + carRect.width(),
            carY + carRect.height()
        )

// Draw the car
        canvas.drawRoundRect(
            carRect,
            20f,   // corner radius X
            20f,   // corner radius Y
            carPaint
        )


        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                handlePress(x, y)
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_CANCEL -> {
                resetControls()
            }
        }
        return true
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

