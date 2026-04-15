package org.example.project

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// Исправленное имя и тип (теперь совпадает с expect в SerialManager.kt)
actual fun findSerialPort() {
    println("Serial port search is not implemented on Android yet")
}