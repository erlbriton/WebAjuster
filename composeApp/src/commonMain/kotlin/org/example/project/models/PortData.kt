package org.example.project.models


// data class идеально подходит для хранения данных
data class PortData(
    val id: String,
    val name: String,
    val status: String
)

/**
 * Модель данных для файла конфигурации.
 * @property fileName Имя файла (напр. "config.ini") — наш уникальный ключ для замены данных.
 * @property id Строка идентификации устройства из файла.
 * @property location Место установки (Location) — ключ для группировки в списке.
 */
data class DeviceInfoIni(
    val fileName: String,
    val LastDateTime: String,
    val id: String,
    val location: String,
    val Description: String,
    val deviceType: String
)