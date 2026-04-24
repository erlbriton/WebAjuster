// Файл: commonMain/kotlin/SerialManager.kt
package org.example.project
/**
 * Функция для подключения к устройству.
 * Возвращает имя выбранного порта или описание ошибки.
 */


expect suspend fun findSerialPort(data: ByteArray)
expect suspend fun triggerPortSelection(): String
expect fun getActivePortName(): String