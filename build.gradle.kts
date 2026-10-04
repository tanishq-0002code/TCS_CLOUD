// =============================================================================
// Telegram Cloud Gallery — Root build script (Kotlin DSL)
// Plugins are declared here and applied in :app
// =============================================================================

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}

// Convenience task: `./gradlew clean` for the whole project.
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}