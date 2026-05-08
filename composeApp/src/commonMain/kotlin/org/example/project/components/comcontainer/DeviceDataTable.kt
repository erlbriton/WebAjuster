package org.example.project.components.comcontainer

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.components.LineFifthTable
import org.example.project.components.LineForthTable
import org.example.project.components.LineThirdTable
import org.example.project.components.LineTwoTable
import org.example.project.models.DeviceInfoIni
import org.example.project.utils.creatorColumn

private data class DisplayRow(
    val hexBase: String,
    val physBase: String,
    val hexCtrl: String,
    val physCtrl: String,
    val rowColor: Color
)

@Composable
fun DeviceDataTable(
    selectedDevice: DeviceInfoIni?,
    tableScrollState: ScrollState,
    innerColumnWeight: Float,
    onInnerResize: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var weightCol2 by remember { mutableStateOf(0.4f) }
    var weightCol3 by remember { mutableStateOf(0.6f) }
    var comparisonWeight by remember { mutableStateOf(0.5f) }
    var hexBase by remember { mutableStateOf(0.5f) }
    var hexlController by remember { mutableStateOf(0.5f) }

    key(selectedDevice?.id) {
        Column(modifier = modifier.fillMaxSize()) {
            LineTwoTable()
            LineThirdTable(selectedDevice = selectedDevice)
            LineForthTable()
            LineFifthTable()

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
                        val displayRows = remember(selectedDevice) {
                            selectedDevice?.flashParameters?.map { param ->
                                val isDifferent = param.hexBase != param.hexCtrl || param.physBase != param.physCtrl
                                DisplayRow(
                                    hexBase = param.hexBase,
                                    physBase = param.physBase,
                                    hexCtrl = param.hexCtrl,
                                    physCtrl = param.physCtrl,
                                    rowColor = if (isDifferent) Color.Red else Color.Black
                                )
                            } ?: emptyList()
                        }

                        Row(modifier = Modifier.fillMaxSize()) {

                            // --- СТОЛБЕЦ БАЗА ---
                            creatorColumn(
                                modifier = Modifier.weight(comparisonWeight),
                                headerTitle = "БАЗА",
                                headerHeight = 25.dp,
                                headerBgColor = Color(0xFFE0E0E0),
                                isResizable = true,
                                onResize = { delta ->
                                    val change = delta / 400f
                                    comparisonWeight = (comparisonWeight + change).coerceIn(0.2f, 0.8f)
                                },
                                content = {
                                    Row(modifier = Modifier.fillMaxSize()) {
                                        // hex (БАЗА) - Оставляем creatorColumn, чтобы была ручка ресайза
                                        creatorColumn(
                                            modifier = Modifier.weight(hexBase),
                                            headerTitle = "hex",
                                            headerHeight = 25.dp,
                                            headerBgColor = Color(0xFFE0E0E0),
                                            isResizable = true,
                                            onResize = { delta ->
                                                val change = delta / 400f
                                                hexBase = (hexBase + change).coerceIn(0.2f, 0.8f)
                                            },
                                            content = {
                                                Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                    displayRows.forEach { row ->
                                                        Text(text = row.hexBase, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().height(20.dp).padding(vertical = 4.dp), color = row.rowColor)
                                                    }
                                                }
                                            }
                                        )
                                        // Physical (БАЗА) - Делаем ОБЫЧНОЙ колонкой без creatorColumn
                                        Column(modifier = Modifier.weight(1f - hexBase).fillMaxHeight()) {
                                            // Рисуем шапку вручную, чтобы не дублировать границы
                                            Box(modifier = Modifier.fillMaxWidth().height(25.dp).background(Color(0xFFE0E0E0)), contentAlignment = Alignment.Center) {
                                                Text("Physical", fontSize = 12.sp)
                                            }
                                            Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                displayRows.forEach { row ->
                                                    Text(text = row.physBase, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().height(20.dp).padding(vertical = 4.dp), color = row.rowColor)
                                                }
                                            }
                                        }
                                    }
                                }
                            )

                            // --- СТОЛБЕЦ КОНТРОЛЛЕР ---
                            creatorColumn(
                                modifier = Modifier.weight(1f - comparisonWeight),
                                headerTitle = "КОНТРОЛЛЕР",
                                headerHeight = 25.dp,
                                headerBgColor = Color(0xFFE0E0E0),
                                isResizable = false,
                                content = {
                                    Row(modifier = Modifier.fillMaxSize()) {
                                        // hex (КОНТРОЛЛЕР) - Оставляем creatorColumn для ресайза
                                        creatorColumn(
                                            modifier = Modifier.weight(hexlController),
                                            headerTitle = "hex",
                                            headerHeight = 25.dp,
                                            headerBgColor = Color(0xFFE0E0E0),
                                            isResizable = true,
                                            onResize = { delta ->
                                                val change = delta / 400f
                                                hexlController = (hexlController + change).coerceIn(0.2f, 0.8f)
                                            },
                                            content = {
                                                Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                    displayRows.forEach { row ->
                                                        Text(text = row.hexCtrl, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().height(20.dp).padding(vertical = 4.dp), color = row.rowColor)
                                                    }
                                                }
                                            }
                                        )
                                        // Physical (КОНТРОЛЛЕР) - Тоже обычной колонкой
                                        Column(modifier = Modifier.weight(1f - hexlController).fillMaxHeight()) {
                                            Box(modifier = Modifier.fillMaxWidth().height(25.dp).background(Color(0xFFE0E0E0)), contentAlignment = Alignment.Center) {
                                                Text("Physical", fontSize = 12.sp)
                                            }
                                            Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                displayRows.forEach { row ->
                                                    Text(text = row.physCtrl, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().height(20.dp).padding(vertical = 4.dp), color = row.rowColor)
                                                }
                                            }
                                        }
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