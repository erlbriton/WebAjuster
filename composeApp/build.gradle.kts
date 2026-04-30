import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop") // Поддержка Desktop (JVM)

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

            // Подключение расширенных иконок
            implementation(compose.materialIconsExtended)
        }

        // ЗАВИСИМОСТИ ДЛЯ DESKTOP (Kubuntu)
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                // Явно добавляем runtime для Linux x64, чтобы избежать ошибки загрузки библиотеки Skiko
                implementation("org.jetbrains.skiko:skiko-awt-runtime-linux-x64:0.8.9")
            }
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

compose.desktop {
    application {
        mainClass = "MainKt" // Ссылается на ваш файл Main.kt
        nativeDistributions {
            targetFormats(TargetFormat.Deb)
            packageName = "Adjuster"
            packageVersion = "1.0.0"
        }
    }
}