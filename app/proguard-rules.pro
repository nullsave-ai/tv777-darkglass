# 777 - Dark Glass Streaming UI ProGuard / R8 Rules

# Preserve Media3 ExoPlayer classes & codecs
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# Jetpack Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# DataStore
-keepclassmembers class * extends androidx.datastore.preferences.core.Preferences { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
}

# Preserve serialized data models if any
-keepclassmembers class com.darkglass.tv777.data.model.** { *; }
