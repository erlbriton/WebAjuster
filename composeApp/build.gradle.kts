import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop") // Поддержка Desktop (JVM) для Kubuntu

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

            // Подключение расширенных иконок для UI
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

compose.desktop {
    application {
        mainClass = "MainKt" // Точка входа в программу

        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe)
            packageName = "org.example.project"
            packageVersion = "1.0.0"

            linux {
                shortcut = true
                menuGroup = "Development"
            }

            windows {
                shortcut = true
                // menuGroup = "Project" // Можно раскомментировать для Windows
            }
        }
    }
}