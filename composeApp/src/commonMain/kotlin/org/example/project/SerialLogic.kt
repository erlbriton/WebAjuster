package org.example.project // Проверь, чтобы совпадало с твоим namespace

object SerialLogic {

    fun calculateCrc16(data: ByteArray): Int {
        var crc = 0xFFFF
        for (pos in data.indices) {
            // Явно приводим к Int и убираем знак перед XOR
            val byteValue = data[pos].toInt() and 0xFF
            crc = crc xor byteValue

            for (i in 0 until 8) {
                if ((crc and 0x0001) != 0) {
                    crc = (crc shr 1) xor 0xA001
                } else {
                    crc = crc shr 1
                }
            }
        }
        return crc
    }

    fun createReadRequest(unitId: Int = 1, startAddress: Int = 0, quantity: Int = 78): ByteArray {
        val buffer = ByteArray(8)
        buffer[0] = unitId.toByte()
        buffer[1] = 0x03
        buffer[2] = (startAddress shr 8).toByte()
        buffer[3] = (startAddress and 0xFF).toByte()
        buffer[4] = (quantity shr 8).toByte()
        buffer[5] = (quantity and 0xFF).toByte()

        val crc = calculateCrc16(buffer.sliceArray(0..5))

        buffer[6] = (crc and 0xFF).toByte()
        buffer[7] = (crc shr 8).toByte()

        return buffer
    }
}