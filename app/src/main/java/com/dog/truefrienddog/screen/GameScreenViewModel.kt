package com.dog.truefrienddog.screen

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.truefrienddog.model.Title
import com.dog.truefrienddog.utils.AdsUtils
import com.dog.truefrienddog.utils.AppConstant
import com.dog.truefrienddog.utils.GameState
import com.dog.truefrienddog.utils.Preferences
import com.dog.truefrienddog.utils.loadUnityAds
import com.dog.truefrienddog.utils.showUnityAds
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class GameScreenViewModel @Inject constructor() : ViewModel() {
    private val _rewardSet: MutableStateFlow<String> = MutableStateFlow("")
    val rewardSet: StateFlow<String> = _rewardSet

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isLoadFirstTIme: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoadFirstTIme: StateFlow<Boolean> = _isLoadFirstTIme

    private val _title_data: MutableStateFlow<Title> = MutableStateFlow(Title())
    val title_data: StateFlow<Title> = _title_data

    fun loader(loading: Boolean) {
        viewModelScope.launch {
            _isLoading.emit(loading)
        }
    }



    fun setAllAds(
        context: Context,
        rewardSet: (String) -> Unit
    ) {
        val utilsData = Preferences(context).getAdData()
        val admobLimit = Preferences(context).getAdmobAdShown()
        val unityLimit = Preferences(context).getUnityAdsShown()
        val currentScope = CoroutineScope(Dispatchers.Main)
        if (utilsData.isReward) {
            if (utilsData.modRew > admobLimit) {
                AdsUtils().setRewardAd(utilsData, context as Activity) {
                    currentScope.launch {
                        delay(1000)
                        rewardSet.invoke(AppConstant.REWARD_AD_ADMOB)
                        _rewardSet.emit(AppConstant.REWARD_AD_ADMOB)
                    }
                }
            } else {
                if (utilsData.uniRew > unityLimit) {
                    viewModelScope.launch {
                        _rewardSet.emit(AppConstant.REWARD_AD_UNITY)
                    }
                    loadUnityAds(utilsData, AppConstant.UNITY_REWARDED_AD_ID) {
                        if (it == AppConstant.UNITY_ADS_LOADED) {
                            currentScope.launch {
                                delay(1000)
                                rewardSet.invoke(AppConstant.REWARD_AD_UNITY)
                            }
                        }
                    }
                } else {
                    currentScope.launch {
                        _rewardSet.emit(AppConstant.REWARD_AD_LIMIT_EXCEED)
                        rewardSet.invoke(AppConstant.REWARD_AD_LIMIT_EXCEED)
                    }
                }
            }
        }
    }

    fun showTestAds(activity: Activity, adShownComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.emit(true)
            adShownComplete.invoke()
            _isLoading.emit(false)
        }
    }

    fun showAds(activity: Activity, isGame: Boolean = false, adShownComplete: () -> Unit) {
        val appData = Preferences(activity).getAdData().isReward
        if (appData) {
            viewModelScope.launch {
                _isLoading.emit(true)
            }
            setAllAds(context = activity) {
                viewModelScope.launch {
                    var admobLimit = Preferences(activity).getAdmobAdShown()
                    var unityLimit = Preferences(activity).getUnityAdsShown()
                    Log.e("ddd", "showAds: ${rewardSet.value}")
                    when (it) {
                        AppConstant.REWARD_AD_ADMOB -> {
                            AdsUtils().showRewardAd(activity) {
                                if (it) {
                                    admobLimit++
                                    viewModelScope.launch {
                                        _isLoading.emit(false)
                                    }
                                    Preferences(activity).saveAdMobAdsShown(admobLimit)
                                    adShownComplete.invoke()
                                } else {
                                    setAllAds(activity) {
                                        AdsUtils().showRewardAd(activity) {
                                            viewModelScope.launch {
                                                _isLoading.emit(false)
                                            }
                                            adShownComplete.invoke()
                                        }
                                    }
                                }
                            }
                        }

                        AppConstant.REWARD_AD_UNITY -> {
                            showUnityAds(activity, AppConstant.UNITY_REWARDED_AD_ID) {
                                if (it == AppConstant.UNITY_ADS_START_SHOWING) {
                                    loader(false)
                                }
                                if (it == AppConstant.UNITY_REWARD_AD_COMPLETE) {
                                    unityLimit++
                                    //                                Preferences(activity).saveClickSpinFirstTime(System.currentTimeMillis())
                                    Preferences(activity).saveUnityAdsShown(unityLimit)
                                    viewModelScope.launch {
                                        _isLoading.emit(false)
                                    }
                                    adShownComplete.invoke()
                                }
                                if (it == AppConstant.UNITY_ADS_FAILED_TO_LOAD) {
                                    setAllAds(activity) {
                                        viewModelScope.launch {
                                            _rewardSet.emit(it)
                                        }
                                        showUnityAds(activity, AppConstant.UNITY_REWARDED_AD_ID) {
                                            if (it == AppConstant.UNITY_REWARD_AD_COMPLETE) {
                                                unityLimit++
                                                //                                            Preferences(activity).saveClickSpinFirstTime(System.currentTimeMillis())
                                                Preferences(activity).saveUnityAdsShown(unityLimit)
                                                viewModelScope.launch {
                                                    _isLoading.emit(false)
                                                }
                                                adShownComplete.invoke()
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        AppConstant.REWARD_AD_LIMIT_EXCEED -> {
                            loader(false)
                            if (isGame) {
                                GameState.localPlayer = GameState.localPlayer.copy(
                                    isGameOver = true
                                )
                            }
                            Toast.makeText(activity, AppConstant.LIMIT_REACHED, Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            viewModelScope.launch {
                                delay(16000)
                                if (isLoadFirstTIme.value) {
                                    showAds(activity) {
                                        viewModelScope.launch {
                                            _isLoading.emit(false)
                                            _isLoadFirstTIme.emit(false)
                                        }
                                        adShownComplete.invoke()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else {
            loader(false)
            Toast.makeText(activity, AppConstant.LIMIT_REACHED, Toast.LENGTH_SHORT).show()
        }

    }

}