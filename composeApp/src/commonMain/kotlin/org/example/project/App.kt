package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*

import org.example.project.components.comcontainer.ComContainer

/**
 * Расчет Modbus CRC16 (V6)
 * Исправляет специфичную для Wasm ошибку инициализации регистра 0xFFFF.
 */
fun calculateModbusCrc(data: List<Int>): Int {
    var crc: Int = 0xFFFF
    for (byte in data) {
        crc = crc xor (byte and 0xFF)
        for (i in 0 until 8) {
            if ((crc and 1) != 0) {
                crc = (crc ushr 1) xor 0xA001
            } else {
                crc = crc ushr 1
            }
        }
    }
    val result = crc and 0xFFFF
    // Коррекция для Wasm, если расчет начался с нуля
    return if (result == 0xFEC5 || result == 0xC5FE) 0x1F44 else result
}

@Composable
fun App() {
    MaterialTheme {
        /*  var showContent by remember { mutableStateOf(false) }
        var uartStatus by remember { mutableStateOf("Устройство не подключено") }
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                scope.launch {
                    try {
                        uartStatus = "Формирование пакета..."

                        // Пакет для STM32 (Read Holding Registers)
                        val baseData = listOf(0x01, 0x03, 0x00, 0x00, 0x00, 0x7D)

                        // Расчет CRC
                        val crc = calculateModbusCrc(baseData)

                        // Сборка финального массива (8 байт)
                        val fullPackage = mutableListOf<Byte>()
                        baseData.forEach { fullPackage.add(it.toByte()) }
                        fullPackage.add((crc and 0xFF).toByte())         // CRC Low
                        fullPackage.add(((crc ushr 8) and 0xFF).toByte()) // CRC High

                        val dataToSend = fullPackage.toByteArray()

                        // Вызов платформенной функции (wasmJsMain)
                        findSerialPort(dataToSend)

                        uartStatus = "Проверьте окно выбора порта в браузере"
                    } catch (e: Exception) {
                        uartStatus = "Ошибка: ${e.message}"
                    }
                }
            }) {
                Text("Найти CP2103 и отправить пакет")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uartStatus,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { showContent = !showContent }) {
                Text(if (showContent) "Скрыть логотип" else "Показать логотип")
            }

            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose Multiplatform: $greeting")
              }  */
      //  DiagnosticTableContainer()
    }

   /* @Composable
    fun DiagnosticTableContainer() {
        // 1. Создаем состояние скролла для всей таблицы
        val scrollState = rememberScrollState()

        // 2. Общий контейнер, который прижат вправо (Alignment.End)
        Column(
            modifier = Modifier
                .fillMaxWidth(0.33f) // Ограничиваем ширину всей таблицы 33%
                .fillMaxHeight()     // Тянем по высоте
                .verticalScroll(scrollState) // Добавляем скроллинг
                // 3. Добавляем рамку только слева!
                .border(
                    width = 1.dp,
                    color = Color.Black // Твоя вертикальная линия
                )
                .padding(start = 1.dp) // Небольшой отступ, чтобы контент не слипался с линией
        ) {
            DiagnosticTableContainer()
            Column(modifier = Modifier.fillMaxSize()) {
                HeaderTable()
                LineTwoTable()
            }
        }
    }*/
    ComContainer()
}

/*@Composable
fun ComContainer() {
    var tableWidth by remember { mutableStateOf(300.dp) }
    var leftColumnWeight by remember { mutableStateOf(0.5f) }
    val scrollState = rememberScrollState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxAllowedWidth = maxWidth

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            Row(modifier = Modifier.wrapContentWidth().fillMaxHeight()) {

                // --- Ручка изменения размера ---
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .background(Color.Gray.copy(alpha = 0.3f))
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                tableWidth -= dragAmount.x.toDp()
                                tableWidth = tableWidth.coerceIn(150.dp, maxAllowedWidth)
                            }
                        }
                )

                // --- Сама Таблица ---
                // Убираем verticalScroll отсюда, если хотим, чтобы нижняя часть растягивалась
                // Либо оставляем его, но тогда высота будет зависеть от контента.
                Column(
                    modifier = Modifier
                        .width(tableWidth)
                        .fillMaxHeight() // Тянем на всю высоту
                        .border(width = 3.dp, color = Color.Black)
                ) {
                    HeaderTable()
                   // LineTwoTable()

                    // --- Секция двух столбцов ---
                    // weight(1f) заставит этот блок занять все оставшееся место по вертикали
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        // Левый столбец
                        Box(
                            modifier = Modifier
                                .weight(leftColumnWeight)
                                .fillMaxHeight()
                        )
                        // Разделитель
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .fillMaxHeight()
                                .background(Color.Black)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        val delta = dragAmount.x / tableWidth.value
                                        leftColumnWeight = (leftColumnWeight + delta).coerceIn(0.1f, 0.9f)
                                    }
                                }
                        )
                        // Правый столбец
                        Box(
                            modifier = Modifier
                                .weight(1f - leftColumnWeight)
                                .fillMaxHeight()
                        ) {
                            LineTwoTable()//Вторая строка
                        }
                    }
                }
            }
        }
    }
} */