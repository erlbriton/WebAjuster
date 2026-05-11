@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package org.example.project

import org.khronos.webgl.Int8Array
import org.khronos.webgl.set

// Твой старый тест FPS
@JsFun("(a, b) => { window.runModbusTest(a, b); }")
external fun jsModbusFpsTest(dataArray: Int8Array, expected: Int)

// Новая внешняя функция для чтения ID
// Мы выносим JS-код в аннотацию @JsFun. Это самый быстрый и стабильный путь в Wasm.
@JsFun("""
    async () => {
        try {
            const port = await navigator.serial.requestPort();
            await port.open({ baudRate: 115200 });
            
            const writer = port.writable.getWriter();
            const reader = port.readable.getReader();

            const request = new Uint8Array([0x01, 0x2B, 0x0E, 0x01, 0x00, 0x70, 0x77]);
            
            console.log("--> Отправка Read Device ID (0x2B)...");
            await writer.write(request);
            writer.releaseLock();

            const { value, done } = await reader.read();
            if (value) {
                const hex = Array.from(value)
                    .map(b => b.toString(16).padStart(2, '0').toUpperCase())
                    .join(' ');
                console.log("<-- Ответ получен (HEX):", hex);
                
                const decoder = new TextDecoder();
                console.log("Текстовая расшифровка (со сдвигом):", decoder.decode(value.slice(6)));
            }
            
            reader.releaseLock();
        } catch (err) {
            console.error("Ошибка Serial API:", err);
        }
    }
""")
external fun jsReadDeviceId()

// Обертки для вызова из Compose
actual suspend fun findSerialPort(data: ByteArray) {
    val i8Array = Int8Array(data.size)
    for (i in data.indices) {
        i8Array.set(i, data[i])
    }
    jsModbusFpsTest(i8Array, 159)
}

// Теперь эта функция просто вызывает внешнюю JS функцию
suspend fun readDeviceIdentification() {
    jsReadDeviceId()
}