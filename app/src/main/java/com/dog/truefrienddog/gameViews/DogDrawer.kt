package com.multiplayer.local.gameViews
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

object DogPaints {

    val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#C68642")
    }

    val earPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#8D5524")
    }

    val legPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#8D5524")
    }

    val tailPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#8D5524")
    }

    val eyePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }

    val nosePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }
}

object DogDrawer {

    fun drawDog(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        bodyPaint: Paint = DogPaints.bodyPaint,
        earPaint: Paint = DogPaints.earPaint,
        eyePaint: Paint = DogPaints.eyePaint,
        nosePaint: Paint = DogPaints.nosePaint,
        scale: Float = 0.4f
    ) {
        // 🟤 Head
        canvas.drawCircle(
            cx,
            cy - 120 * scale,
            120 * scale,
            bodyPaint
        )

        // 👂 Ears
        canvas.drawOval(
            cx - 160 * scale, cy - 260 * scale,
            cx - 60 * scale, cy - 100 * scale,
            earPaint
        )
        canvas.drawOval(
            cx + 60 * scale, cy - 260 * scale,
            cx + 160 * scale, cy - 100 * scale,
            earPaint
        )

        // 👀 Eyes
        canvas.drawCircle(
            cx - 40 * scale, cy - 140 * scale,
            12 * scale,
            eyePaint
        )
        canvas.drawCircle(
            cx + 40 * scale, cy - 140 * scale,
            12 * scale,
            eyePaint
        )

        // 👃 Nose
        canvas.drawCircle(
            cx,
            cy - 110 * scale,
            14 * scale,
            nosePaint
        )
    }
}
