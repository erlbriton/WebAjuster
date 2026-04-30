package org.example.project

actual suspend fun findSerialPort(data: ByteArray) {
    // Пустая реализация для компиляции
    println("Desktop: findSerialPort вызван с данными размером ${data.size}")
}