plugins {
    // Оставьте только то, что реально используется в модулях
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false

    // УДАЛИТЕ ЭТИ СТРОКИ, если они больше не используются в подпроектах:
    // alias(libs.plugins.androidApplication) apply false
    // alias(libs.plugins.androidLibrary) apply false
}
