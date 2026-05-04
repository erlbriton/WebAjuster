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
        val fullContent = result.content // Весь текст файла

        // 1. Разбиваем текст на строки
        val lines = fullContent.split("\n", "\r")

        // 2. Ищем строку, которая начинается с "Location=" (игнорируя регистр)
        val locationValue = lines
            .firstOrNull { it.trim().startsWith("Location=", ignoreCase = true) }
            ?.substringAfter("=") // Берем всё, что после знака "="
            ?.trim() ?: ""        // Если не нашли, оставляем пустым

        println("DEBUG: Найдено значение Location: $locationValue")

        // 3. Теперь в DeviceInfo передаем только извлеченное значение
        DeviceInfo(
            id = result.name,
            location = locationValue // Теперь здесь только путь/значение, а не весь файл
        )
    } catch (e: Throwable) {
        println("DEBUG: Ошибка парсинга: ${e.message}")
        null
    }
}

actual suspend fun pickDirectory(): DeviceInfo? = null