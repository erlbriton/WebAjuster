package org.example.project.actionsButton

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.actions.HeaderActions
import org.example.project.utils.DeviceInfo
import org.example.project.utils.pickDirectory
import org.example.project.utils.pickSingleFile

class HeaderActionsButtons(
    private val scope: CoroutineScope,
    private val onDeviceLoaded: (DeviceInfo) -> Unit,
    private val ShowError: (String) -> Unit
) : HeaderActions {

    override fun onUpdate() {
        println("Обновление списка устройств...")
    }

    override fun onSearch() {
        println("Запуск поиска Modbus...")
    }

    override fun onOpenOscillograph() {
        println("Открытие осциллографа...")
    }

    override fun onTerminalOpen() {
        println("Запуск терминала...")
    }

    override fun onFileSave() {
        println("Сохранение данных...")
    }

    override fun onHelp(topic: String) {
        println("Вызов справки по теме: $topic")
    }

    override fun onMemoryChanged(type: String) {
        println("Область памяти изменена на: $type")
    }

    override fun onDeviceDataLoaded(info: DeviceInfo) {
        // В WEB проверяем и location, и id (имя файла)
        val path = info.location.trim().lowercase()
        val fileName = info.id.trim().lowercase()

        // Если хотя бы одно из полей заканчивается на .ini или .txt
        if (path.endsWith(".ini") || path.endsWith(".txt") ||
            fileName.endsWith(".ini") || fileName.endsWith(".txt")) {

            onDeviceLoaded(info)
        } else {
            // Если не подошло, показываем ошибку с именем файла
            val displayIdentifier = if (info.id.isNotEmpty()) info.id else "Неизвестный файл"
            ShowError("Ошибка формата!\nФайл: $displayIdentifier\nРазрешены только .ini и .txt")
        }
    }

    override fun onPickFileRequest() {
        scope.launch {
            val result = pickSingleFile()
            if (result != null) onDeviceDataLoaded(result)
        }
    }

    override fun onPickDirectoryRequest() {
        scope.launch {
            delay(100)
            val result = pickDirectory()
            if (result != null) {
                onDeviceDataLoaded(result)
            }
        }
    }
}