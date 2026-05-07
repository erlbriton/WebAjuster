import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    // УДАЛЕНО: jvm("desktop") - поддержка Desktop убрана

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
        // ОБЩИЕ ЗАВИСИМОСТИ (commonMain)
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
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

            // Подключение расширенных иконок для UI
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
        }

        // УДАЛЕНО: val desktopMain - блок зависимостей Desktop убран

        // ЗАВИСИМОСТИ ТОЛЬКО ДЛЯ WEB (WasmJs)
        val wasmJsMain by getting {
            dependencies {
                // Стабильные версии JS-библиотек для работы со временем
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

// УДАЛЕНО: compose.desktop { ... } - весь блок конфигурации приложения Desktop убран