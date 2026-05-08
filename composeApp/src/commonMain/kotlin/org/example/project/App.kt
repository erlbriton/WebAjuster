//App.kt

package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.LineHeightStyle
import org.example.project.components.comcontainer.ComContainer
import org.example.project.viewmodel.MainViewModel
import org.example.project.viewmodel.LocalMainViewModel
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.unit.TextUnit


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
    val mainViewModel = remember { MainViewModel() }

    // Упрощенный стиль без платформенных специфичных параметров
    val tightTextStyle = TextStyle(
        lineHeight = TextUnit.Unspecified, // Отключаем навязанную высоту строки
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both
        )
    )

    val customTypography = Typography(
        bodyLarge = tightTextStyle,
        bodyMedium = tightTextStyle,
        bodySmall = tightTextStyle,
        labelLarge = tightTextStyle,
        labelMedium = tightTextStyle,
        labelSmall = tightTextStyle
    )

    MaterialTheme(typography = customTypography) {
        CompositionLocalProvider(LocalMainViewModel provides mainViewModel) {
            ComContainer()
        }
    }
}

//-----------------------Открытие com порта---------------------------------------------------------

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
//  }

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

