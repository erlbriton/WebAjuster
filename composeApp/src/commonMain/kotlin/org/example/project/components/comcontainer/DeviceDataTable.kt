package org.example.project.components.comcontainer

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    var comparisonWeight by remember { mutableStateOf(0.5f) }//База - Контроллер
    var hexBase by remember { mutableStateOf(0.5f) }//Для Базы
    var hexlController by remember { mutableStateOf(0.5f) }//Для Контроллера

    key(selectedDevice?.id) {
        Column(modifier = modifier.fillMaxSize()) {
            // Верхние информационные панели
            LineTwoTable()
            LineThirdTable()
            LineForthTable()
            LineFifthTable()

            // ГЛАВНАЯ СТРОКА ТАБЛИЦЫ
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {

                // 1. СЕКЦИЯ "ПАРАМЕТРЫ"
                creatorColumn(
                    modifier = Modifier.weight(innerColumnWeight),
                    headerTitle = "ПАРАМЕТРЫ",
                    headerHeight = 25.dp,
                    headerBgColor = Color(0xFFE0E0E0),
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
                        )
                    }
                )

                // 2. СЕКЦИЯ "СРАВНЕНИЕ ДАННЫХ"
                creatorColumn(
                    modifier = Modifier.weight(1f - innerColumnWeight),
                    headerTitle = "",
                    headerHeight = 0.dp,
                    content = {
                        Row(modifier = Modifier.fillMaxSize()) {

                            // --- Столбец БАЗА ---
                            creatorColumn(
                                modifier = Modifier.weight(comparisonWeight), // Используем переменную
                                headerTitle = "БАЗА",
                                headerHeight = 25.dp,
                                headerBgColor = Color(0xFFE0E0E0),
                                isResizable = true,
                                onResize = { delta ->
                                    // Вычисляем, насколько сдвинулся вес в зависимости от ширины экрана
                                    // Можно подобрать делитель (например, 400f), чтобы ресайз был плавным
                                    val change = delta / 400f
                                    comparisonWeight =
                                        (comparisonWeight + change).coerceIn(0.2f, 0.8f)
                                },
                                content = {
                                    Row(modifier = Modifier.fillMaxSize()) {

                                        // --- Столбец hex ---
                                        creatorColumn(
                                            modifier = Modifier.weight(hexBase), // Используем переменную
                                            headerTitle = "hex",
                                            headerHeight = 25.dp,
                                            headerBgColor = Color(0xFFE0E0E0),
                                            isResizable = true,
                                            onResize = { delta ->
                                                // Вычисляем, насколько сдвинулся вес в зависимости от ширины экрана
                                                // Можно подобрать делитель (например, 400f), чтобы ресайз был плавным
                                                val change = delta / 400f
                                                hexBase =
                                                    (hexBase + change).coerceIn(0.2f, 0.8f)
                                            },
                                            content = {
                                            }
                                        )
                                        // --- Столбец Physical ---
                                        creatorColumn(
                                            modifier = Modifier.weight(1f - hexBase), // Остаток пространства
                                            headerTitle = "Physical",
                                            headerHeight = 25.dp,
                                            headerBgColor = Color(0xFFE0E0E0),
                                            isResizable = false, // Второму столбцу ресайз не нужен, он подстроится сам
                                            onResize = {},
                                            content = {
                                            }
                                        )
                                    }
                                }
                            )
                            // --- Столбец КОНТРОЛЛЕР ---
                            creatorColumn(
                                modifier = Modifier.weight(1f - comparisonWeight), // Остаток пространства
                                headerTitle = "КОНТРОЛЛЕР",
                                headerHeight = 25.dp,
                                headerBgColor = Color(0xFFE0E0E0),
                                isResizable = false, // Второму столбцу ресайз не нужен, он подстроится сам
                                onResize = {},
                                content = {
                                           Row(modifier = Modifier.fillMaxSize()) {
                                        //-------------------Столбец hex----------------------------
                                        creatorColumn(
                                            modifier = Modifier.weight(hexlController), // Используем переменную
                                            headerTitle = "hex",
                                            headerHeight = 25.dp,
                                            headerBgColor = Color(0xFFE0E0E0),
                                            isResizable = true,
                                            onResize = { delta ->
                                                // Вычисляем, насколько сдвинулся вес в зависимости от ширины экрана
                                                // Можно подобрать делитель (например, 400f), чтобы ресайз был плавным
                                                val change = delta / 400f
                                                hexlController =
                                                    (hexlController + change).coerceIn(0.2f, 0.8f)
                                            },
                                            content = {
                                            }
                                        )
                                        // --- Столбец Physical ---
                                        creatorColumn(
                                            modifier = Modifier.weight(1f - hexlController), // Остаток пространства
                                            headerTitle = "Physical",
                                            headerHeight = 25.dp,
                                            headerBgColor = Color(0xFFE0E0E0),
                                            isResizable = false, // Второму столбцу ресайз не нужен, он подстроится сам
                                            onResize = {},
                                            content = {
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}