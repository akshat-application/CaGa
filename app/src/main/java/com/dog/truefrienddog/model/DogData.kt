package com.dog.truefrienddog.model

import com.dog.truefrienddog.utils.Aes
import com.google.gson.Gson

//import org.bouncycastle.pqc.crypto.sphincsplus.SPHINCSPlusKeyPairGenerator
//import org.bouncycastle.pqc.crypto.sphincsplus.SPHINCSPlusKeyGenerationParameters
//import org.bouncycastle.crypto.AsymmetricCipherKeyPair
//import org.bouncycastle.pqc.crypto.sphincsplus.SPHINCSPlusParameters
//import org.bouncycastle.pqc.crypto.sphincsplus.SPHINCSPlusPrivateKeyParameters
//import org.bouncycastle.pqc.crypto.sphincsplus.SPHINCSPlusPublicKeyParameters
//import org.bouncycastle.util.encoders.Hex
//import java.security.SecureRandom

data class DogData(
    var idThis: String = "",
    var isShowOpenAd: Boolean = false,
    var isShowAds: Boolean = false,
    var isShowUnity: Boolean = false,
    var isShowMainButtonAds: Boolean = false,
    var admobAdLimit: Int = 2,
    var unityAdLimit: Int = 2,
    var isTest: Boolean = true,
    var isShowRewardAd: Boolean = true,
    var showWhitePaper: Boolean = false,
    var isShowSupport: Boolean = false,
    var python: Boolean = true,
    var questionUrl: String = "",
    var rewardAdId: String = "",
    var generatedText: String = "",
    var openAdId: String = ""
)
//real id unitu ad idThis 5862574
data class PlayerInfo (
    var playerName: String = "", var rank: String = "",
    var winCondition: String = "", var score: String = "0",
    var dateTime: String = "",
    var allPlayerScore: List<Pair<String,String>> = listOf(),
    var yourRank: String = "",
    var name: String = "",
    var isMultiPlayer: Boolean = false,
)

data class QuestionList(
    var questionList: List<Questions> = listOf()
)

data class Questions(
    var question: String = "", var option1: String = "", var option2: String = "",
    var option3: String = "", var option4: String = "", var answer: String = ""
)

data class GetAllUrls(
    var dogData: String = "",
    var titleUrl: String = "",
    var wallData: String = "",
    var questions: String = "",
    var server: String = "",
)

data class ListDataGenerateText(
    var listText: List<String> = listOf()
)

data class StoreData (
    var phrase: String = "",
    var public_key: String = "",
    var address: String = "",
    var signature: String = "",
    var is_valid: Int = 2
)

data class Title(
    var Title: String = "",
    var description: String = "",
    var wallData: String = "",
    var isBa: Boolean = false,
    var isVisibleSup: Boolean = false,
    var supDis: String = "",
    var Add: String = ""
)