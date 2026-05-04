package org.example.project.utils

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import org.example.project.utils.DeviceInfo

/**
 * Описываем структуру объекта из JS.
 * Добавлено поле isAlreadyTxt, чтобы понимать, нужно ли делать копию.
 */
external interface JsFileResult : JsAny {
    val name: String
    val content: String
    val isAlreadyTxt: Boolean // Флаг, который мы добавили в serial_logic.js
}

// Функции, объявленные в вашем serial_logic.js
external fun showFilePickerNative(callback: (JsAny?) -> Unit)
external fun saveFileAsTxt(originalName: String, content: String)

actual suspend fun pickSingleFile(): DeviceInfo? {
    val handle = suspendCoroutine<JsAny?> { cont ->
        showFilePickerNative { res -> cont.resume(res) }
    } ?: return null

    return parseIniFile(handle)
}

private fun parseIniFile(handle: JsAny?): DeviceInfo? {
    return try {
        val result = handle as JsFileResult
        val fullContent = result.content

        // 1. Извлекаем только строку Location= для отображения в левом столбце
        val lines = fullContent.split("\n", "\r")
        val locationValue = lines
            .firstOrNull { it.trim().startsWith("Location=", ignoreCase = true) }
            ?.substringAfter("=")
            ?.trim() ?: ""

        // 2. ПРОВЕРКА: Если файл НЕ является .txt, то вызываем сохранение копии.
        // Это предотвратит повторное сохранение, если вы уже открыли .txt файл.
        if (!result.isAlreadyTxt) {
            println("DEBUG: Файл .ini — инициируем сохранение .txt копии")
            saveFileAsTxt(result.name, fullContent)
        } else {
            println("DEBUG: Файл .txt — пропущено автоматическое сохранение")
        }

        // Возвращаем объект для UI
        DeviceInfo(
            id = result.name,
            location = locationValue
        )
    } catch (e: Throwable) {
        println("DEBUG: Ошибка парсинга в Kotlin: ${e.message}")
        null
    }
}

actual suspend fun pickDirectory(): DeviceInfo? = null