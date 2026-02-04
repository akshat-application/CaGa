plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    alias(libs.plugins.kotlin.hilt)
}

android {
    namespace = "com.dog.truefrienddog"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.dog.truefrienddog"
        minSdk = 24
        targetSdk = 36
        versionCode = 17
        versionName = "17"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    //    ads
    implementation("com.google.android.gms:play-services-ads:23.2.0")
    implementation("com.unity3d.ads:unity-ads:4.7.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    // Retrofit
    implementation("com.squareup.okhttp3:okhttp:3.14.9")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    //    material
    implementation("androidx.compose.material:material:1.6.7")
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
    implementation("com.google.android.material:material:1.12.0")

//    material 3
    implementation ("androidx.compose.material3:material3-window-size-class:1.1.2")
    implementation ("androidx.compose.material3:material3")

    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt(libs.hilt.android)

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.7.0")

    kapt(libs.hilt.compiler)
    //   image loader
    implementation("io.coil-kt:coil-compose:2.3.0")
    implementation("io.coil-kt:coil-gif:2.3.0")

    //  for okhttp3's BouncyCastlePlatform
    implementation("org.bouncycastle:bcprov-jdk15to18:1.77")
    implementation("org.bouncycastle:bctls-jdk15to18:1.77")
// okhttp3's Conscrypt Platform
    implementation("org.openjsse:openjsse:1.1.2")

}