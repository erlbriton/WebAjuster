import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            outputModuleName.set("composeApp")
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        // ОБЩИЕ ЗАВИСИМОСТИ
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.coroutines.core)

            // ПРАВИЛЬНЫЙ СПОСОБ ПОДКЛЮЧЕНИЯ ИКОНОК В MULTIPLATFORM:
            // Эти переменные берутся из плагина composeMultiplatform
            implementation(compose.materialIconsExtended)
        }

        // ЗАВИСИМОСТИ ТОЛЬКО ДЛЯ WEB (WasmJs)
        val wasmJsMain by getting {
            dependencies {
                implementation(npm("@js-joda/core", "3.2.0"))
                implementation(npm("@js-joda/timezone", "2.3.0"))
                implementation(libs.kotlinx.browser)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}