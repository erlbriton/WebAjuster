window.activePort = null;

window.runModbusTest = async function(dataArray, expectedBytes) {
    if (!navigator.serial) return;

    // Если порт не выбран через "Поиск", пробуем запросить его тут (обратная совместимость)
    if (!window.activePort) {
        try {
            window.activePort = await navigator.serial.requestPort();
        } catch (e) { return; }
    }

    const port = window.activePort;
    const requestData = new Uint8Array(dataArray.buffer, dataArray.byteOffset, dataArray.byteLength);
    let frames = 0;
    let fpsLastReport = performance.now();

    navigator.serial.addEventListener("disconnect", (event) => {
        if (event.target === port) {
            console.error("%c--- УСТРОЙСТВО ФИЗИЧЕСКИ ИЗВЛЕЧЕНО ---", "color: red;");
            window.activePort = null;
        }
    });

    while (true) {
        let reader = null;
        let writer = null;
        try {
            if (port.readable || port.writable) await port.close().catch(() => {});
            await port.open({ baudRate: 115200, bufferSize: 16384 });
            console.log("%c--- СЕССИЯ ВОССТАНОВЛЕНА ---", "color: #00ff00; font-weight: bold;");

            while (true) {
                reader = port.readable.getReader();
                writer = port.writable.getWriter();
                try {
                    await writer.write(requestData);
                    let responseBuffer = new Uint8Array(0);
                    let startTime = performance.now();
                    while (responseBuffer.length < expectedBytes) {
                        if (performance.now() - startTime > 300) throw new Error("Link Timeout");
                        const { value, done } = await reader.read();
                        if (done) throw new Error("Port Closed");
                        if (value) {
                            let newBuf = new Uint8Array(responseBuffer.length + value.length);
                            newBuf.set(responseBuffer);
                            newBuf.set(value, responseBuffer.length);
                            responseBuffer = newBuf;
                        }
                    }
                    if (responseBuffer.length >= expectedBytes) {
                        frames++;
                        if (window.onModbusData) window.onModbusData(responseBuffer.slice(0, expectedBytes));
                    }
                } finally {
                    if (writer) writer.releaseLock();
                    if (reader) { await reader.cancel().catch(() => {}); reader.releaseLock(); }
                }
                await new Promise(r => setTimeout(r, 1));
            }
        } catch (err) {
            console.error("%cОШИБКА: " + err.message, "color: #ffaa00;");
            try { await port.close().catch(() => {}); } catch(e) {}
            await new Promise(r => setTimeout(r, 1500));
        }
    }
};

window.requestUsbPort = async function(callback) {
    try {
        window.activePort = await navigator.serial.requestPort();
        const info = window.activePort.getInfo();
        callback("USB: " + info.usbVendorId + ":" + info.usbProductId);
    } catch (e) {
        callback("No Port");
    }
};