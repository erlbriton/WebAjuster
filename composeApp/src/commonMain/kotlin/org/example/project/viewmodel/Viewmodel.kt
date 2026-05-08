package org.example.project.viewmodel

import androidx.compose.runtime.*

class MainViewModel {
    // Состояние для типа механизма
    var typeMechanism by mutableStateOf("")

    // Состояние для даты
    var dateSet by mutableStateOf("29.01.1964")

    // Функции обновления
    fun updateMechanism(newValue: String) {
        typeMechanism = newValue
    }

    fun updateDate(newValue: String) {
        dateSet = newValue
    }
}

// Ключ для доступа через CompositionLocal
val LocalMainViewModel = staticCompositionLocalOf<MainViewModel> {
    error("ViewModel не предоставлена!")
}