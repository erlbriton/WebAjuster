package org.example.project.actions // Новый путь

import org.example.project.utils.DeviceInfo

/**
 * Интерфейс для обработки всех событий заголовка.
 * Вынесен в отдельный пакет 'actions' для удобства масштабирования.
 */
interface HeaderActions {
    // Операции с устройствами
    fun onUpdate()
    fun onSearch()
    fun onOpenOscillograph()

    // Системные операции
    fun onTerminalOpen()
    fun onFileSave()
    fun onHelp(topic: String)

    // Выбор памяти
    fun onMemoryChanged(type: String)

    // Работа с файловой системой (вызывается из UI)
    fun onPickFileRequest()
    fun onPickDirectoryRequest()

    // Коллбэк для возврата данных обратно в UI или логику
    fun onDeviceDataLoaded(info: DeviceInfo)
}