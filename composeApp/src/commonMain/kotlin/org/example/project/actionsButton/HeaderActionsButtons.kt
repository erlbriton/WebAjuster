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
    private val onDeviceLoaded: (DeviceInfo) -> Unit
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
        // Проверяем расширение файла перед тем, как передать его дальше
        val path = info.location.lowercase()
        if (path.endsWith(".ini") || path.endsWith(".txt")) {
            onDeviceLoaded(info)
        } else {
            println("Ошибка: Выбран некорректный тип файла. Разрешены только .ini и .txt")
            // Здесь в будущем можно добавить вызов диалогового окна с ошибкой
        }
    }

    override fun onPickFileRequest() {
        scope.launch {
            delay(100)

            // Теперь pickSingleFile вызывается без жестких фильтров (или с расширенными),
            // чтобы пользователь мог выбрать любой файл, а проверка прошла в onDeviceDataLoaded.
            val result = pickSingleFile()

            if (result != null) {
                onDeviceDataLoaded(result)
            }
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