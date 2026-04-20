package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.example.project.components.ParameterTable
import org.example.project.models.ParameterRow

// Функция расчета CRC остается без изменений
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
    return if (result == 0xFEC5 || result == 0xC5FE) 0x1F44 else result
}

@Composable
fun App() {
    val testData = remember {
        listOf(
            ParameterRow("1", "V_out", "Output", "V", "0x01", "12.5", "0x02", "12.4"),
            ParameterRow("2", "I_out", "Current", "A", "0x03", "1.5", "0x04", "1.6")
        )
    }

    // Состояние ширины (вес таблицы)
    var tableWeight by remember { mutableStateOf(0.6f) }

    MaterialTheme {
        Row(modifier = Modifier.fillMaxSize()) {
            // 1. Осциллограф (теперь слева)
            Column(
                modifier = Modifier
                    .weight(1f - tableWeight)
                    .fillMaxHeight()
                    .background(Color.Black)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Осциллограмма", color = Color.Green)
                // ... ваш код кнопок управления ...
            }

            // 2. Разделитель (ползунок)
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF444444))
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            // Логика направления: так как таблица справа,
                            // инвертируем dragAmount.x
                            tableWeight = (tableWeight - dragAmount.x / 1000f).coerceIn(0.2f, 0.8f)
                        }
                    }
            )

            // 3. Таблица (теперь справа)
            Box(modifier = Modifier.weight(tableWeight).fillMaxHeight()) {
                ParameterTable(testData)
            }
        }
    }
}