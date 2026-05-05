package org.example.project.actionsButton

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.actions.HeaderActions
import org.example.project.models.DeviceInfoIni
import org.example.project.utils.pickDirectory
import org.example.project.utils.pickSingleFile

class HeaderActionsButtons(
    private val scope: CoroutineScope,
    private val onDeviceLoaded: (DeviceInfoIni) -> Unit,
    private val ShowError: (String) -> Unit
) : HeaderActions {

    override fun onUpdate() {
        println("Обновление списка устройств...")
    }

    override fun onSearch() {
        println("Запуск поиска Modbus...")
    }

    override fun onExel(){
        println("Генератор отчетов  Exel")
    }

            override fun onOpenOscillograph() {
        println("Открытие осциллографа...")
    }

    override fun onTerminalOpen() {
        println("Запуск терминала...")
    }

    override fun onBlackBox(){
        println("Просмотр черного ящика")
    }

    override fun onFileOration() {
        println("Сохранение данных...")
    }

    override fun onHelp(topic: String) {
        println("Вызов справки по теме: $topic")
    }

    override fun onMemoryChanged(type: String) {
        println("Область памяти изменена на: $type")
    }

    override fun onDeviceDataLoaded(info: DeviceInfoIni) {
        // Ошибка была здесь: проверять нужно fileName, а не id или location
        val name = info.fileName.trim().lowercase()

        // Если имя файла заканчивается на .ini или .txt — это наш файл
        if (name.endsWith(".ini") || name.endsWith(".txt")) {
            onDeviceLoaded(info)
        } else {
            // Если не подошло, показываем ошибку с именем файла
            val displayIdentifier = if (info.fileName.isNotEmpty()) info.fileName else "Неизвестный файл"
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
            // 1. Теперь ждем список файлов вместо одного объекта
            val results: List<DeviceInfoIni>? = pickDirectory()

            // 2. Если файлы найдены, передаем их в UI через существующий колбэк
            results?.forEach { info ->
                onDeviceDataLoaded(info)
            }
        }
    }

}