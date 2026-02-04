package com.multiplayer.local.gameViews

import android.graphics.Color
import android.graphics.Paint

data class DogColorSkin(
    val body: Paint,
    val ear: Paint,
    val leg: Paint,
    val tail: Paint,
    val eye: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.BLACK },
    val nose: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.BLACK }
)

fun paint(color: Int) =
    Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color }

val dogColorSkins = listOf(
    DogColorSkin(
        body = paint(Color.parseColor("#C68642")),
        ear = paint(Color.parseColor("#8D5524")),
        leg = paint(Color.parseColor("#8D5524")),
        tail = paint(Color.parseColor("#8D5524"))
    ),
    DogColorSkin(
        body = paint(Color.WHITE),
        ear = paint(Color.LTGRAY),
        leg = paint(Color.LTGRAY),
        tail = paint(Color.LTGRAY)
    ),

    DogColorSkin(
        body = paint(Color.parseColor("#FFD966")),
        ear = paint(Color.parseColor("#C9A227")),
        leg = paint(Color.parseColor("#C9A227")),
        tail = paint(Color.parseColor("#C9A227"))
    ),

    DogColorSkin(
        body = paint(Color.RED),
        ear = paint(Color.parseColor("#ed587d")),
        leg = paint(Color.parseColor("#ed587d")),
        tail = paint(Color.parseColor("#ed587d"))
    ),

    DogColorSkin(
        body = paint(Color.parseColor("#4FC3F7")),
        ear = paint(Color.parseColor("#0288D1")),
        leg = paint(Color.parseColor("#0288D1")),
        tail = paint(Color.parseColor("#0288D1"))
    ),

    DogColorSkin(
        body = paint(Color.parseColor("#7CFC00")),
        ear = paint(Color.parseColor("#228B22")),
        leg = paint(Color.parseColor("#228B22")),
        tail = paint(Color.parseColor("#228B22"))
    ),

    DogColorSkin(
        body = paint(Color.parseColor("#BA68C8")),
        ear = paint(Color.parseColor("#6A1B9A")),
        leg = paint(Color.parseColor("#6A1B9A")),
        tail = paint(Color.parseColor("#6A1B9A"))
    ),

    DogColorSkin(
        body = paint(Color.parseColor("#9CCC65")),
        ear = paint(Color.parseColor("#558B2F")),
        leg = paint(Color.parseColor("#558B2F")),
        tail = paint(Color.parseColor("#558B2F"))
    ),

    DogColorSkin(
        body = paint(Color.GRAY),
        ear = paint(Color.DKGRAY),
        leg = paint(Color.DKGRAY),
        tail = paint(Color.DKGRAY)
    ),
    DogColorSkin(
        body = paint(Color.parseColor("#e87407")),
        ear = paint(Color.parseColor("#e8a464")),
        leg = paint(Color.parseColor("#e8a464")),
        tail = paint(Color.parseColor("#e8a464"))
    )
)

