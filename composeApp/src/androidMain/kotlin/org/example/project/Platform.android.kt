package org.example.project

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// Добавьте вот это:
actual fun findSerialPort() {
    println("Serial port search is not implemented on Android")
}