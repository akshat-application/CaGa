package com.dog.truefrienddog.gameViews

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Tree(
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

private val treeTrunkPaint = Paint().apply {
    color = Color.rgb(120, 80, 50)
}

private val treeLeafPaint = Paint().apply {
    color = Color.rgb(30, 140, 30)
    isAntiAlias = true
}

fun drawTree(canvas: Canvas, x: Float, y: Float, type: Int) {
    when (type) {
        0 -> drawTree(canvas,x,y)
        1 -> drawPalmTree(canvas, x, y)
        2 -> drawJungleOak(canvas, x, y)
        else -> drawBush(canvas, x, y)
    }
}

private val leafPaint = Paint().apply {
    color = Color.parseColor("#228B22") // Forest Green
    style = Paint.Style.FILL
    isAntiAlias = true // Makes the tree edges smooth
}

private val trunkPaint = Paint().apply {
    color = Color.parseColor("#5D4037") // Dark Brown
    style = Paint.Style.FILL
    isAntiAlias = true
}

fun drawJungleOak(canvas: Canvas, x: Float, y: Float) {
    // 1. Draw Trunk
    val trunkWidth = 15f
    val trunkHeight = 50f
    canvas.drawRect(x - trunkWidth/2, y, x + trunkWidth/2, y + trunkHeight, trunkPaint)

    // 2. Draw Leafy Top (3 overlapping circles for a "cloud" look)
    canvas.drawCircle(x, y - 10f, 35f, leafPaint)       // Center leaf
    canvas.drawCircle(x - 25f, y + 5f, 25f, leafPaint)  // Left leaf
    canvas.drawCircle(x + 25f, y + 5f, 25f, leafPaint)  // Right leaf
}

fun drawBush(canvas: Canvas, x: Float, y: Float) {
    // A bush doesn't need a trunk, just low-hanging ovals
    val rect = RectF(x - 40f, y + 20f, x + 40f, y + 50f)
    canvas.drawOval(rect, leafPaint)

    // Add a smaller oval on top to make it look 3D
    val topRect = RectF(x - 20f, y + 10f, x + 20f, y + 35f)
    canvas.drawOval(topRect, leafPaint)
}

fun drawPalmTree(canvas: Canvas, x: Float, y: Float) {
    // Trunk
    canvas.drawRect(x - 5f, y, x + 5f, y + 40f, treeTrunkPaint)
    // Leaves (Drawn as arcs or triangles)
    canvas.drawOval(x - 30f, y - 20f, x + 30f, y + 10f, treeLeafPaint)
}
fun drawTree(canvas: Canvas, x: Float, y: Float) {

    canvas.drawRect(
        x - 6f,
        y,
        x + 6f,
        y + 35f,
        treeTrunkPaint
    )

    canvas.drawCircle(
        x,
        y,
        20f,
        treeLeafPaint
    )
}