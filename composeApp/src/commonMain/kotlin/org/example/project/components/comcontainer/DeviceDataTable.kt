package org.example.project.components.comcontainer

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    var selectedRowIndex by remember { mutableStateOf(-1) }

    key(selectedDevice?.id) {
        Column(modifier = modifier.fillMaxSize()) {
            LineTwoTable()
            LineThirdTable(selectedDevice = selectedDevice)
            LineForthTable()
            LineFifthTable()

            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Row(modifier = Modifier.fillMaxSize()) {

                    // 1. ПАРАМЕТРЫ (Левая часть)
                    creatorColumn(
                        modifier = Modifier.weight(innerColumnWeight),
                        headerTitle = "ПАРАМЕТРЫ",
                        headerHeight = 20.dp,
                        headerBgColor = Color(0xFFE0E0E0),
                        isResizable = true,
                        onResize = onInnerResize,
                        content = {
                            Row(modifier = Modifier.fillMaxSize()) {
                                creatorColumn(
                                    modifier = Modifier.width(55.dp),
                                    headerTitle = "№",
                                    headerHeight = 20.dp,
                                    isResizable = false,
                                    content = {
                                        Column(Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                            selectedDevice?.flashParameters?.forEachIndexed { i, p ->
                                                ComparisonCell(p.code, Color.Black, i == selectedRowIndex) { selectedRowIndex = i }
                                            }
                                        }
                                    }
                                )
                                creatorColumn(
                                    modifier = Modifier.weight(weightCol2),
                                    headerTitle = "Имя",
                                    headerHeight = 20.dp,
                                    isResizable = true,
                                    onResize = { delta ->
                                        val change = delta / 500f
                                        weightCol2 = (weightCol2 + change).coerceIn(0.1f, 0.8f)
                                        weightCol3 = (1.0f - weightCol2)
                                    },
                                    content = {
                                        Column(Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                            selectedDevice?.flashParameters?.forEachIndexed { i, p ->
                                                ComparisonCell(p.idName, Color.Black, i == selectedRowIndex, textAlign = TextAlign.Start) { selectedRowIndex = i }
                                            }
                                        }
                                    }
                                )
                                creatorColumn(
                                    modifier = Modifier.weight(weightCol3),
                                    headerTitle = "Описание",
                                    headerHeight = 20.dp,
                                    isResizable = false,
                                    content = {
                                        Column(Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                            selectedDevice?.flashParameters?.forEachIndexed { i, p ->
                                                ComparisonCell(p.description, Color.Black, i == selectedRowIndex, textAlign = TextAlign.Start) { selectedRowIndex = i }
                                            }
                                        }
                                    }
                                )
                                creatorColumn(
                                    modifier = Modifier.width(55.dp),
                                    headerTitle = "Ед.изм",
                                    headerHeight = 20.dp,
                                    isResizable = false,
                                    dividerThickness = 0.dp,
                                    content = {
                                        Column(Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                            selectedDevice?.flashParameters?.forEachIndexed { i, p ->
                                                ComparisonCell(p.unit, Color.Black, i == selectedRowIndex) { selectedRowIndex = i }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    )

                    // 2. СРАВНЕНИЕ (Правая часть)
                    Box(modifier = Modifier.weight(1f - innerColumnWeight)) {
                        val displayRows = remember(selectedDevice) {
                            selectedDevice?.flashParameters?.map { param ->
                                val isDifferent = param.hexBase != param.hexCtrl || param.physBase != param.physCtrl
                                DisplayRow(param.hexBase, param.physBase, param.hexCtrl, param.physCtrl, if (isDifferent) Color.Red else Color.Black)
                            } ?: emptyList()
                        }

                        Row(modifier = Modifier.fillMaxSize()) {
                            // БАЗА
                            creatorColumn(
                                modifier = Modifier.weight(comparisonWeight),
                                headerTitle = "БАЗА",
                                headerHeight = 20.dp,
                                headerBgColor = Color(0xFFE0E0E0),
                                isResizable = true,
                                onResize = { delta -> comparisonWeight = (comparisonWeight + delta / 400f).coerceIn(0.2f, 0.8f) },
                                content = {
                                    Row(modifier = Modifier.fillMaxSize()) {
                                        creatorColumn(
                                            modifier = Modifier.weight(hexBase),
                                            headerTitle = "hex",
                                            headerHeight = 20.dp,
                                            isResizable = true,
                                            onResize = { delta -> hexBase = (hexBase + delta / 400f).coerceIn(0.2f, 0.8f) },
                                            content = {
                                                Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                    displayRows.forEachIndexed { i, row -> ComparisonCell(row.hexBase, row.rowColor, i == selectedRowIndex) { selectedRowIndex = i } }
                                                }
                                            }
                                        )
                                        creatorColumn(
                                            modifier = Modifier.weight(1f - hexBase),
                                            headerTitle = "Physical",
                                            headerHeight = 20.dp,
                                            isResizable = false,
                                            dividerThickness = 0.dp,
                                            content = {
                                                Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                    displayRows.forEachIndexed { i, row -> ComparisonCell(row.physBase, row.rowColor, i == selectedRowIndex) { selectedRowIndex = i } }
                                                }
                                            }
                                        )
                                    }
                                }
                            )
                            // КОНТРОЛЛЕР
                            creatorColumn(
                                modifier = Modifier.weight(1f - comparisonWeight),
                                headerTitle = "КОНТРОЛЛЕР",
                                headerHeight = 20.dp,
                                headerBgColor = Color(0xFFE0E0E0),
                                isResizable = false,
                                content = {
                                    Row(modifier = Modifier.fillMaxSize()) {
                                        creatorColumn(
                                            modifier = Modifier.weight(hexlController),
                                            headerTitle = "hex",
                                            headerHeight = 20.dp,
                                            isResizable = true,
                                            onResize = { delta -> hexlController = (hexlController + delta / 400f).coerceIn(0.2f, 0.8f) },
                                            content = {
                                                Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                    displayRows.forEachIndexed { i, row -> ComparisonCell(row.hexCtrl, row.rowColor, i == selectedRowIndex) { selectedRowIndex = i } }
                                                }
                                            }
                                        )
                                        creatorColumn(
                                            modifier = Modifier.weight(1f - hexlController),
                                            headerTitle = "Physical",
                                            headerHeight = 20.dp,
                                            isResizable = false,
                                            dividerThickness = 0.dp,
                                            content = {
                                                Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                    displayRows.forEachIndexed { i, row -> ComparisonCell(row.physCtrl, row.rowColor, i == selectedRowIndex) { selectedRowIndex = i } }
                                                }
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(tableScrollState),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .padding(top = 20.dp), // СМЕЩАЕМ ВНИЗ, чтобы он начинался под заголовками
                    style = ScrollbarStyle(
                        minimalHeight = 16.dp,
                        thickness = 10.dp,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
                        hoverDurationMillis = 300,
                        unhoverColor = Color.Yellow.copy(alpha = 0.5f),
                        hoverColor = Color.Cyan
                    )
                )
            }
        }
    }
}

@Composable
private fun ComparisonCell(
    text: String,
    textColor: Color,
    isSelected: Boolean,
    textAlign: TextAlign = TextAlign.Center,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .background(if (isSelected) Color.Cyan.copy(alpha = 0.25f) else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = if (textAlign == TextAlign.Start) Alignment.CenterStart else Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            textAlign = textAlign,
            color = textColor,
            modifier = Modifier.padding(horizontal = 4.dp),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
    }
}