package com.multiplayer.local.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.multiplayer.local.model.PlayerInfo
import com.multiplayer.local.model.SeData
import com.multiplayer.local.model.Title
import com.multiplayer.local.model.Type
import com.google.gson.Gson

class Preferences(context: Context) {
    val type: Type = Type()
    val context1 = context

    private val MY_PREFERNECE_NAME = "THDFDJKJKDJFJFKDFJ"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(MY_PREFERNECE_NAME, Context.MODE_PRIVATE)
    private val preferenceEditor: SharedPreferences.Editor = sharedPreferences.edit()

    private val CLICK_FIRST_TIME = "DDFFJKDFDJKDKKJKDJF"
    fun saveAdData(data: String) {
        preferenceEditor.putString(CLICK_FIRST_TIME, data)
        preferenceEditor.apply()
    }

    fun getAdData(): SeData {
        val gson = Gson().toJson(SeData())
        val tEncrypt = Aes.encryptObject(gson)
        val saved = sharedPreferences.getString(CLICK_FIRST_TIME, tEncrypt)
        val decrypt = Aes.decryptObject(saved!!)
        val seData: SeData = Gson().fromJson(decrypt, type.di())
        return seData
    }

    private val ADMOB_SHOW = "savehhhh_FDDFJKDFJKDJFKDJ"
    fun saveAdMobAdsShown(data: Int) {
        val encrypt = Aes.encryptObject(data.toString())
        preferenceEditor.putString(ADMOB_SHOW, encrypt)
        preferenceEditor.apply()
    }

    fun getAdmobAdShown(): Int {
        val saved = sharedPreferences.getString(ADMOB_SHOW, Aes.encryptObject("0"))
        return Aes.decryptObject(saved!!).toInt()
    }

    private val UNITY_SHOW = "Asllk_FDDFJKDFJKDJFKDJ"
    fun saveUnityAdsShown(data: Int) {
        val encrypt = Aes.encryptObject(data.toString())
        preferenceEditor.putString(UNITY_SHOW, encrypt)
        preferenceEditor.apply()
    }

    fun getUnityAdsShown(): Int {
        val saved = sharedPreferences.getString(UNITY_SHOW, Aes.encryptObject("0"))
        return Aes.decryptObject(saved!!).toInt()
    }

    private val TITLE_OF_DATA = "TITLE_OF_DATA_DFJJFKDDGH"
    fun saveTitleData(data: String) {
        preferenceEditor.putString(TITLE_OF_DATA, data)
        preferenceEditor.apply()
    }

    fun getTitleData(): Title {
        var title: Title = Title()
        val gson = Gson().toJson(Title())
        val encrypt = Aes.encryptObject(gson)
        val savedData = sharedPreferences.getString(TITLE_OF_DATA, encrypt)
        try {
            val decryptData = Aes.decryptObject(savedData!!)
            title = Gson().fromJson(decryptData, type.ti())
        } catch (_: Exception) {

        }
        return title
    }

    private val SAVE_COIN_DATA = "ffgggkhlg;gf_FDDFJKDFJKDJFKDJ"
    fun saveCoinData(data: Float) {
        val encrypt = Aes.encryptObject(data.toString())
        preferenceEditor.putString(SAVE_COIN_DATA, encrypt)
        preferenceEditor.apply()
    }

    fun getCoinData(): Float {
        val value = Aes.encryptObject("0")
        return Aes.decryptObject(sharedPreferences.getString(SAVE_COIN_DATA, value)!!).toFloat()
    }

    private val SINGLE_PLAYER_HISTROY = "dddffggggghhhhhhh"
    fun saveSinglePlayerHistory(urlNewModel: PlayerInfo) {
        val listUserAstro = mutableListOf<PlayerInfo>()
        val data: List<PlayerInfo> = Preferences(context1).getSinglePlayerHistory()
        if (data.isNotEmpty()) {
            listUserAstro.addAll(data)
            val iteratorListUserAstro = listUserAstro.listIterator()

            while (iteratorListUserAstro.hasNext()) {
                val element = iteratorListUserAstro.next()
                if (element == PlayerInfo()) {
                    iteratorListUserAstro.remove()
                }
            }
            iteratorListUserAstro.add(urlNewModel)
            val gson = Gson().toJson(listUserAstro)
            val encrypt = Aes.encryptObject(gson)
            preferenceEditor.putString(SINGLE_PLAYER_HISTROY, encrypt)
            preferenceEditor.apply()
        } else {
            listUserAstro.add(urlNewModel)
            val gson = Gson().toJson(listUserAstro)
            val encrypt = Aes.encryptObject(gson)
            preferenceEditor.putString(SINGLE_PLAYER_HISTROY, encrypt)
            preferenceEditor.apply()
        }
    }

    fun getSinglePlayerHistory(): List<PlayerInfo> {
        val savedData = sharedPreferences.getString(
            SINGLE_PLAYER_HISTROY,
            listOf("").toString()
        )
        try {
            val data = Aes.decryptObject(savedData ?: "")
            return Gson().fromJson(data, type.inf())
        } catch (e: Exception) {
            return listOf()
        }
    }

    private val ROCKET_BOOSTER_TIME = "ROCKET_BOOSTER_TIMEFDFDF01D"
    fun saveCurrentTime(totalBalance: String) {
        val encrypt = Aes.encryptObject(totalBalance)
        preferenceEditor.putString(ROCKET_BOOSTER_TIME, encrypt)
        preferenceEditor.apply()
    }

    fun isTimeNotEnded(): Boolean {
        val currentTime = System.currentTimeMillis()
        val encryptCurrentTime = Aes.encryptObject("0")
        val getWatchAdTime = sharedPreferences.getString(ROCKET_BOOSTER_TIME, encryptCurrentTime)
        val decryptData = Aes.decryptObject(getWatchAdTime!!).toLong().plus(86400000)
        Log.e(TAG, "onCreadddddddte: ${decryptData}")
        Log.e(TAG, "onCreadddddddte: ${currentTime}")
        return if (decryptData != null) {
            decryptData < currentTime
        } else {
            false
        }
    }

    private val MULTI_PLAYER_HISTROY = "MFFGGHdddffggggghhhhhhh"
    fun saveMultiPlayerHistory(urlNewModel: PlayerInfo) {
        val listUserAstro = mutableListOf<PlayerInfo>()
        val data: List<PlayerInfo> = Preferences(context1).getMultiPlayerHistory()
        if (data.isNotEmpty()) {
            listUserAstro.addAll(data)
            val iteratorListUserAstro = listUserAstro.listIterator()

            while (iteratorListUserAstro.hasNext()) {
                val element = iteratorListUserAstro.next()
                if (element == PlayerInfo()) {
                    iteratorListUserAstro.remove()
                }
            }
            iteratorListUserAstro.add(urlNewModel)
            val gson = Gson().toJson(listUserAstro)
            val encrypt = Aes.encryptObject(gson)
            preferenceEditor.putString(MULTI_PLAYER_HISTROY, encrypt)
            preferenceEditor.apply()
        } else {
            listUserAstro.add(urlNewModel)
            val gson = Gson().toJson(listUserAstro)
            val encrypt = Aes.encryptObject(gson)
            preferenceEditor.putString(MULTI_PLAYER_HISTROY, encrypt)
            preferenceEditor.apply()
        }
    }

    fun getMultiPlayerHistory(): List<PlayerInfo> {
        val savedData = sharedPreferences.getString(
            MULTI_PLAYER_HISTROY,
            listOf("").toString()
        )
        try {
            val data = Aes.decryptObject(savedData ?: "")
            return Gson().fromJson(data, type.inf())
        } catch (e: Exception) {
            return listOf()
        }
    }

}