// =============================================================================
// Telegram Cloud Gallery — Gradle Settings (Kotlin DSL)
// =============================================================================

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // All repositories are declared here (Kotlin DSL only — no Groovy build scripts).
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()

        // -------------------------------------------------------------------------
        // JitPack — provides the PRE-COMPILED TDLib Android AAR.
        // Because an AAR (with prebuilt jni/*.so for arm64-v8a, armeabi-v7a, x86_64)
        // is consumed directly, the project needs NO NDK / CMake / C++ compilation.
        // -------------------------------------------------------------------------
        maven {
            name = "JitPack"
            url = uri("https://jitpack.io")
            content {
                includeGroup("com.github.tdlibx")
                includeGroupByRegex("com\\.github\\..*")
            }
        }
    }
}

rootProject.name = "TelegramCloudGallery"

include(":app")