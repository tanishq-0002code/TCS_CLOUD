import java.util.Properties

// =============================================================================
// Telegram Cloud Gallery — :app module
// =============================================================================

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

/** Reads `key=value` pairs from the root gradle.properties. */
val gradleProps = Properties().apply {
    val f = rootProject.file("gradle.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

fun prop(key: String, fallback: String): String =
    (project.findProperty(key) as String?)?.takeIf { it.isNotBlank() } ?: fallback

// --- TDLib dependency: JitPack AAR (default) or Maven Central mirror -----------
val useMavenTdlib = prop("tdlib.source", "jitpack").equals("maven", ignoreCase = true)

android {
    namespace = "com.example.telegramcloudgallery"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.telegramcloudgallery"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // TDLib credentials, surfaced through BuildConfig so they never live in
        // source files. Override in gradle.properties / ~/.gradle/gradle.properties.
        buildConfigField(
            "int",
            "TDLIB_API_ID",
            "${prop("telegram.api.id", "94575").trim().toInt()}"
        )
        buildConfigField(
            "String",
            "TDLIB_API_HASH",
            "\"${prop("telegram.api.hash", "a3406de8d171bb422bb6ddf3bbd800e2").trim()}\""
        )

        // Only the ABIs the prebuilt TDLib AAR ships (keeps the APK small and
        // guarantees a matching libtdjni.so is always present).
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
        }
    }

    // Required so BuildConfig is generated (disabled globally in gradle.properties).
    buildFeatures {
        compose = true
        buildConfig = true
    }

    // -------------------------------------------------------------------------
    // TDLib's JNI layer requires these to be extractable / mmap-able.
    // -------------------------------------------------------------------------
    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/DEPENDENCIES",
                "/META-INF/INDEX.LIST",
                "META-INF/*.kotlin_module"
            )
        }
        jniLibs {
            useLegacyPackaging = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi"
            )
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/kotlin")
        }
        getByName("test") {
            java.srcDirs("src/test/kotlin")
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    // -------------------------------------------------------------------------
    // TDLib — pre-compiled AAR, ZERO NDK/CMake compilation.
    // Both coordinates below expose `org.drinkless.tdlib.{Client, TdApi}`.
    // -------------------------------------------------------------------------
    if (useMavenTdlib) {
        implementation("io.github.tdlib-android:core:0.1.1")
    } else {
        implementation(libs.tdlib) // com.github.tdlibx:td:1.8.56  (JitPack)
    }

    // --- AndroidX foundation -------------------------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.android)

    // --- Lifecycle (ViewModel / ProcessLifecycleOwner) ----------------------
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.process)

    // --- Compose (BOM-managed) ----------------------------------------------
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // --- Navigation Compose --------------------------------------------------
    implementation(libs.androidx.navigation.compose)

    // --- Room (offline index, folder tree, tags) -----------------------------
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)

    // --- WorkManager (background sync) ---------------------------------------
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.androidx.work.testing)

    // --- Media3 (ExoPlayer + Transformer) -----------------------------------
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.transformer)
    implementation(libs.androidx.media3.effect)

    // --- Security ------------------------------------------------------------
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.biometric)

    // --- Image loading -------------------------------------------------------
    implementation(libs.coil.compose)
    implementation(libs.coil.video)

    // --- Test ---------------------------------------------------------------
    testImplementation("junit:junit:4.13.2")
    testImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

// Room schema export (version control friendly migrations)
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
}