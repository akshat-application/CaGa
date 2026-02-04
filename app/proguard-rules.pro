# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class org.openjsse.** { *; }
-keep class org.bouncycastle.** { *; }
-keep class org.conscrypt.** { *; }

-dontwarn org.bouncycastle.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**
-dontwarn kotlin.jvm.internal.**
-keep class kotlin.jvm.internal.** { *; }

-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**


-keep class com.android.org.conscrypt.** { *; }
-keep class org.apache.harmony.xnet.provider.jsse.** { *; }

-keep class com.multiplayer.local.model** { *; }
-dontwarn com.multiplayer.local.model**

-keep class com.multiplayer.local.utils.Type** { *; }
-dontwarn com.multiplayer.local.utils.Type**