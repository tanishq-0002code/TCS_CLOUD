# =============================================================================
# ProGuard / R8 rules
# =============================================================================

# ---------------------------------------------------------------------------
# TDLib — the JNI bridge resolves these reflectively from native code, so they
# must survive shrinking/obfuscation exactly as-is.
# ---------------------------------------------------------------------------
-keep class org.drinkless.tdlib.** { *; }
-keep class org.drinkless.tdlib.TdApi$* { *; }
-keepclasseswithmembernames class org.drinkless.tdlib.** {
    native <methods>;
}
-dontwarn org.drinkless.tdlib.**

# ---------------------------------------------------------------------------
# Room
# ---------------------------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase { *; }
-dontwarn androidx.room.paging.**

# ---------------------------------------------------------------------------
# Media3 / ExoPlayer — selected classes are resolved by name at runtime.
# ---------------------------------------------------------------------------
-dontwarn androidx.media3.**
-keep class androidx.media3.exoplayer.** { *; }

# ---------------------------------------------------------------------------
# WorkManager workers are instantiated reflectively by class name.
# ---------------------------------------------------------------------------
-keep class * extends androidx.work.CoroutineWorker { <init>(...); }
-keep class * extends androidx.content.ContentObserver { <init>(...); }

# ---------------------------------------------------------------------------
# Kotlin metadata / coroutines
# ---------------------------------------------------------------------------
-keepattributes *Annotation*, InnerClasses, Signature, RuntimeVisible*Annotations
-dontwarn kotlinx.coroutines.**