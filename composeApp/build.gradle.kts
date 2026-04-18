import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    // Удален плагин androidApplication
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    // Удален блок androidTarget

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            // Устанавливаем четкое имя модуля для index.html
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
        }

        // Блок androidMain.dependencies полностью удален

        // ЗАВИСИМОСТИ ТОЛЬКО ДЛЯ WEB (WasmJs)
        val wasmJsMain by getting {
            dependencies {
                // Возвращаем стабильную версию 3.2.0
                implementation(npm("@js-joda/core", "3.2.0"))
                // Добавим часовые пояса на всякий случай
                implementation(npm("@js-joda/timezone", "2.3.0"))
                implementation("org.jetbrains.compose.material:material:1.10.3")
                implementation(libs.kotlinx.browser)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

// Весь блок android { ... } удален, так как он больше не нужен