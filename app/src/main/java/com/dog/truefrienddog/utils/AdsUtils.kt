package com.dog.truefrienddog.utils

import android.app.Activity
import android.util.Log
import com.dog.truefrienddog.model.SeData
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAds.UnityAdsLoadError
import com.unity3d.ads.UnityAdsShowOptions
import com.unity3d.services.core.properties.ClientProperties.getApplicationContext


private var mInterstitialAd: InterstitialAd? = null
private var appOpenAd: AppOpenAd? = null
private var mRewardAd: RewardedAd? = null

class AdsUtils {

    fun loadOpenAppAd(context: Activity, adSet: () -> Unit) {
        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            context, AppConstant.AD_OPEN_APP_ID, adRequest,
            object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: AppOpenAd) {
                    appOpenAd = interstitialAd
                    adSet.invoke()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    adSet.invoke()
                }
            })
    }

    fun showOpenAppAd(context: Activity) {
        if (appOpenAd != null) {
            appOpenAd!!.setImmersiveMode(true)
            appOpenAd!!.show(context)
            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                }
            }
        }
    }

    fun setAds(context: Activity, adSet: () -> Unit) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load (
            context, AppConstant.AD_INTERSTITIAL_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    adSet.invoke()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    adSet.invoke()
//                    mInterstitialAd = null
                }
            }
        )
    }

    fun showInterstitialAd(context: Activity, reward: (Boolean) -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.setImmersiveMode(true)
            mInterstitialAd!!.show(context)
            mInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                }
            }
        }
    }

    fun setRewardAd(utilsData: SeData,context: Activity, adSet: () -> Unit) {
        val adRequest = AdRequest.Builder().build()
        val id_reward = if (utilsData.isTest) {
            AppConstant.AD_TEST_REWARD_ID
        } else {
            AppConstant.AD_REWARD_ID
        }
        RewardedAd.load(
            context, id_reward, adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: RewardedAd) {
                    mRewardAd = interstitialAd
                    adSet.invoke()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    adSet.invoke()
//                    mRewardAd = null
                }
            })
    }

    fun showRewardAd(context: Activity, reward: (Boolean) -> Unit) {
        if (mRewardAd != null) {
            mRewardAd!!.setImmersiveMode(true)
            mRewardAd!!.show(context) {
                reward.invoke(true)
                mRewardAd = null

            }
            mRewardAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                }
            }
        } else {
            reward.invoke(false)
//            Toast.makeText(context, "Internet Not Working Properly", Toast.LENGTH_SHORT).show()
        }
    }
}

fun loadUnityAds(utilsData: SeData, adUnitId: String, onUnityAdLoaded: ((String) -> Unit)) {
    val ad_id = adUnitId
    val unityGameID = utilsData.isIdThis
    val testMode = utilsData.isTest
    val loadListenerADS: IUnityAdsLoadListener = object : IUnityAdsLoadListener {
        override fun onUnityAdsAdLoaded(placementId: String) {
            onUnityAdLoaded.invoke(AppConstant.UNITY_ADS_LOADED)
        }

        override fun onUnityAdsFailedToLoad(
            placementId: String,
            error: UnityAdsLoadError,
            message: String
        ) {
            Log.e(
                "UnityAdsExample",
                "Unity Ads failed to load ad for $placementId with error: [$error] $message"
            )
        }
    }
    val loadListener: IUnityAdsInitializationListener = object : IUnityAdsInitializationListener {
        override fun onInitializationComplete() {
            UnityAds.load(ad_id, loadListenerADS)
        }


        override fun onInitializationFailed(
            error: UnityAds.UnityAdsInitializationError?,
            message: String?
        ) {
//            onUnityAdLoaded.invoke(AppConstant.UNITY_ADS_ERROR)
        }
    }
    UnityAds.initialize(getApplicationContext(), unityGameID, testMode, loadListener)
}

fun showUnityAds(context: Activity, adUnitType: String, onReward: ((String) -> Unit)) {
    val showListener: IUnityAdsShowListener = object : IUnityAdsShowListener {
        override fun onUnityAdsShowFailure(
            placementId: String,
            error: UnityAds.UnityAdsShowError,
            message: String
        ) {
            onReward.invoke(AppConstant.UNITY_ADS_FAILED_TO_LOAD)
        }

        override fun onUnityAdsShowStart(placementId: String) {
            onReward.invoke(AppConstant.UNITY_ADS_START_SHOWING)
            Log.v(
                "UnityAdsExample", "" +
                        "onUnityAdsShowStart: $placementId"
            )
        }

        override fun onUnityAdsShowClick(placementId: String) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: $placementId")
        }

        override fun onUnityAdsShowComplete(
            placementId: String,
            state: UnityAds.UnityAdsShowCompletionState
        ) {
            if (state.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED)) {
                onReward.invoke(AppConstant.UNITY_REWARD_AD_COMPLETE)
            } else {
//                user skipped video
            }
        }
    }
    UnityAds.show(
        context,
        adUnitType,
        UnityAdsShowOptions(),
        showListener
    )
}



