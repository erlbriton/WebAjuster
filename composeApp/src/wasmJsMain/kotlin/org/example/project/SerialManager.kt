package org.example.project

import org.khronos.webgl.Int8Array
import org.khronos.webgl.set // Импорт обязателен для работы оператора [] =

@JsFun("(a, b) => { window.runModbusTest(a, b); }")
external fun jsModbusFpsTest(dataArray: Int8Array, expected: Int)

actual suspend fun findSerialPort(data: ByteArray) {
    val i8Array = Int8Array(data.size)
    for (i in data.indices) {
        // Используем .toByte(), чтобы убедиться в совместимости типов
        // В Wasm это иногда требует явного вызова set
        i8Array.set(i, data[i])
    }

    jsModbusFpsTest(i8Array, 159)// С 259 не работает
}