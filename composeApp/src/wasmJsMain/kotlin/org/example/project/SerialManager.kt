//SerialManager.kt

package org.example.project

import org.khronos.webgl.Int8Array
import org.khronos.webgl.set
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@JsFun("(a, b) => { window.runModbusTest(a, b); }")
external fun jsModbusFpsTest(dataArray: Int8Array, expected: Int)

@JsFun("(callback) => { window.requestUsbPort(callback); }")
external fun jsjsRequestUsbPort(callback: (String) -> Unit)

@JsFun("() => { if (!window.activePort) return 'Not Connected'; const info = window.activePort.getInfo(); return 'USB: ' + info.usbVendorId + ':' + info.usbProductId; }")
external fun jsGetActivePortName(): String

actual suspend fun findSerialPort(data: ByteArray) {
    val i8Array = Int8Array(data.size)
    for (i in data.indices) {
        i8Array.set(i, data[i])
    }
    jsModbusFpsTest(i8Array, 159)
}

actual suspend fun triggerPortSelection(): String = suspendCancellableCoroutine { cont ->
    jsjsRequestUsbPort { result ->
        cont.resume(result)
    }
}

actual fun getActivePortName(): String = jsGetActivePortName()