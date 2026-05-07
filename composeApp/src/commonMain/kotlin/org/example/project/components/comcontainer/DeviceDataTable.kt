package org.example.project.components.comcontainer

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.project.components.LineFifthTable
import org.example.project.components.LineForthTable
import org.example.project.components.LineThirdTable
import org.example.project.components.LineTwoTable
import org.example.project.models.DeviceInfoIni
import org.example.project.utils.creatorColumn

@Composable
fun DeviceDataTable(
    selectedDevice: DeviceInfoIni?,
    tableScrollState: ScrollState,
    innerColumnWeight: Float,       // Вес левой части (Параметры) относительно правой (Сравнение)
    onInnerResize: (Float) -> Unit, // Ресайз общего разделителя
    modifier: Modifier = Modifier
) {
    // Внутренние веса для колонок внутри секции параметров
    var weightCol2 by remember { mutableStateOf(0.4f) } // Name
    var weightCol3 by remember { mutableStateOf(0.6f) } // Description

    key(selectedDevice?.id) {
        Column(modifier = modifier.fillMaxSize()) {
            // Верхние информационные панели
            LineTwoTable()
            LineThirdTable()
            LineForthTable()
            LineFifthTable()

            // ГЛАВНАЯ СТРОКА ТАБЛИЦЫ
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {

                // 1. СЕКЦИЯ "ПАРАМЕТР"
                creatorColumn(
                    modifier = Modifier.weight(innerColumnWeight),
                    headerTitle = "Параметр",
                    headerHeight = 25.dp,
                    headerBgColor = Color(0xFFDFE5CA),
                    isResizable = true,
                    onResize = onInnerResize,
                    content = {
                        ParameterSection(
                            selectedDevice = selectedDevice,
                            tableScrollState = tableScrollState,
                            weightCol2 = weightCol2,
                            weightCol3 = weightCol3,
                            onNameResize = { delta ->
                                // Изменяем баланс между Name и Description
                                val change = delta / 500f
                                weightCol2 = (weightCol2 + change).coerceIn(0.1f, 0.8f)
                                weightCol3 = (1.0f - weightCol2)
                            }
                            // onDescResize удален, так как ручка теперь только одна
                        )
                    }
                )

                // 2. СЕКЦИЯ "СРАВНЕНИЕ ДАННЫХ"
                creatorColumn(
                    modifier = Modifier.weight(1f - innerColumnWeight),
                    headerTitle = "Сравнение данных",
                    headerHeight = 25.dp,
                    headerBgColor = Color(0xFFE0E0E0),
                    content = {
                        // Здесь скоро будет ValueComparisonSection
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .border(0.5.dp, Color.LightGray)
                        ) {
                            Text(
                                "Область сравнения (База/Контроллер)",
                                modifier = Modifier.padding(10.dp),
                                color = Color.Gray
                            )
                        }
                    }
                )
            }
        }
    }
}