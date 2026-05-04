package org.example.project.utils

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import org.example.project.utils.DeviceInfo
import org.example.project.models.DeviceInfoIni

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

actual suspend fun pickSingleFile(): DeviceInfoIni? {
    val handle = suspendCoroutine<JsAny?> { cont ->
        showFilePickerNative { res -> cont.resume(res) }
    } ?: return null

    return parseIniFile(handle)
}

private fun parseIniFile(handle: JsAny?): DeviceInfoIni? {
    return try {
        val result = handle as JsFileResult
        val fullContent = result.content
        val lines = fullContent.split("\n", "\r").map { it.trim() }

        // Извлекаем данные (если строки нет — пишем пустую строку для бейджа)
        val locationValue = lines.findValue("Location=")
        val idValue = lines.findValue("ID=") ?: "No ID"
        val descValue = lines.findValue("Description=")
        val typeValue = lines.findValue("DeviceType=")
        val dateValue = lines.findValue("LastDateTime=") // Или берем текущую, если в файле нет

        if (!result.isAlreadyTxt) {
            saveFileAsTxt(result.name, fullContent)
        }

        DeviceInfoIni(
            fileName = result.name, // Наш уникальный ключ-имя файла
            id = idValue,
            location = locationValue,
            Description = descValue,
            deviceType = typeValue,
            LastDateTime = dateValue
        )
    } catch (e: Throwable) {
        null
    }
}

// Маленький помощник для чистоты кода
private fun List<String>.findValue(prefix: String): String {
    return this.firstOrNull { it.startsWith(prefix, ignoreCase = true) }
        ?.substringAfter("=")?.trim() ?: ""
}

actual suspend fun pickDirectory(): DeviceInfoIni? = null