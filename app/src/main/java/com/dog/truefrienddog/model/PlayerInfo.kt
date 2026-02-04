package com.multiplayer.local.model

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class SeData(
    val isOpen: Boolean = false,
    val isMid: Boolean = true,
    val isReward: Boolean = true,
    val isIdThis: String = "5862574",
    val isTest: Boolean = false,
    val uniRew: Int = 2,
    val modRew: Int = 2,
)

class Type {
    fun di(): Type? {
        return object : TypeToken<SeData>() {}.type
    }

    fun ti(): Type? {
        return object : TypeToken<Title>() {}.type
    }

    fun inf(): Type? {
        return object : TypeToken<List<PlayerInfo>>() {}.type
    }
}
