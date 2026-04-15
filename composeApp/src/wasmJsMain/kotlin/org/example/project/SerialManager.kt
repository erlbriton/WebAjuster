package org.example.project

// Объявляем внешнюю функцию, которая будет реализована на стороне JS
external fun askForSerialPort()

actual fun findSerialPort() {
    // Просто вызываем внешнюю функцию
    askForSerialPort()
}