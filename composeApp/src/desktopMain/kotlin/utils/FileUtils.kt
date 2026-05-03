package org.example.project.utils

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

// Реализация для выбора папки
actual suspend fun pickDirectory(): DeviceInfo? {
    val chooser = JFileChooser().apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        dialogTitle = "Выберите папку с проектом"
        isAcceptAllFileFilterUsed = false
    }

    val result = chooser.showOpenDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        val selectedDir = chooser.selectedFile
        return scanForIni(selectedDir)
    }
    return null
}

// Реализация для выбора одного файла
actual suspend fun pickSingleFile(): DeviceInfo? {
    val chooser = JFileChooser().apply {
        fileSelectionMode = JFileChooser.FILES_ONLY
        dialogTitle = "Выберите .ini файл"
        fileFilter = FileNameExtensionFilter("Конфигурация (.ini, .txt)", "ini", "txt")
        isAcceptAllFileFilterUsed = false
    }

    val result = chooser.showOpenDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        return parseIniFile(chooser.selectedFile)
    }
    return null
}

// Вспомогательная функция для поиска файла в папке
private fun scanForIni(dir: File): DeviceInfo? {
    val files = dir.listFiles() ?: return null
    // Сначала ищем в корне выбранной папки
    val iniFile = files.find { it.extension.lowercase() == "ini" }
    if (iniFile != null) return parseIniFile(iniFile)

    // Если в корне нет, заглядываем на один уровень вглубь (как в вашей логике Wasm)
    files.filter { it.isDirectory }.forEach { subDir ->
        val found = subDir.listFiles()?.find { it.extension.lowercase() == "ini" }
        if (found != null) return parseIniFile(found)
    }
    return null
}

// Вспомогательная функция парсинга
private fun parseIniFile(file: File): DeviceInfo? {
    try {
        var id = ""
        var location = ""
        file.readLines().forEach { line ->
            val cleanLine = line.trim()
            if (cleanLine.startsWith("ID=")) id = cleanLine.substringAfter("=")
            if (cleanLine.startsWith("Location=")) location = cleanLine.substringAfter("=")
        }
        return DeviceInfo(id, location)
    } catch (e: Exception) {
        return null
    }
}