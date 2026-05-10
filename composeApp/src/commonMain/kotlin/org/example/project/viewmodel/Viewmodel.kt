package org.example.project.viewmodel

import androidx.compose.runtime.*

class MainViewModel {
    // Состояние для типа механизма
    var typeMechanism by mutableStateOf("")
    // Состояние для даты
    var dateSet by mutableStateOf("29.01.1964")
    //Переменная для хранения активного (подсвеченного) ID
    var selectedDeviceId by mutableStateOf("")

    fun updateMechanism(newValue: String) {
        typeMechanism = newValue
    }
    fun updateDate(newValue: String) {
        dateSet = newValue
    }

    var installationLocation by mutableStateOf("")

    fun updateDinstallationLocation(newValue: String) {
        installationLocation = newValue
    }
}
// Ключ для доступа через CompositionLocal
val LocalMainViewModel = staticCompositionLocalOf<MainViewModel> {
    error("ViewModel не предоставлена!")
}

val varsMap = mutableStateMapOf<String, Double>()