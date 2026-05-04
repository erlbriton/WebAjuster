package org.example.project.utils

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import org.example.project.utils.DeviceInfo

/**
 * Описываем структуру того объекта, который мы только что создали в JS
 */
external interface JsFileResult : JsAny {
    val name: String
    val content: String
}

external fun showFilePickerNative(callback: (JsAny?) -> Unit)

actual suspend fun pickSingleFile(): DeviceInfo? {
    val handle = suspendCoroutine<JsAny?> { cont ->
        showFilePickerNative { res -> cont.resume(res) }
    } ?: return null

    return parseIniFile(handle)
}

private fun parseIniFile(handle: JsAny?): DeviceInfo? {
    return try {
        val result = handle as JsFileResult
        val fullContent = result.content // Полный текст со всеми данными

        // Логика для левого столбца (как мы делали раньше)
        val lines = fullContent.split("\n", "\r")
        val locationValue = lines
            .firstOrNull { it.trim().startsWith("Location=", ignoreCase = true) }
            ?.substringAfter("=")
            ?.trim() ?: ""

        // СРАЗУ СОХРАНЯЕМ КОПИЮ .txt
        // originalName — это v1.03-RNTTE.2500...ini
        saveFileAsTxt(result.name, fullContent)

        DeviceInfo(
            id = result.name,
            location = locationValue
        )
    } catch (e: Throwable) {
        println("DEBUG: Ошибка: ${e.message}")
        null
    }
}

actual suspend fun pickDirectory(): DeviceInfo? = null

external fun saveFileAsTxt(originalName: String, content: String)