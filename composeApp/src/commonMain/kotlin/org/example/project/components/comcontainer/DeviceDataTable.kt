// DeviceDataTable.kt

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
    var selectedRowIndex by remember { mutableStateOf(-1) }

    key(selectedDevice?.id) {
        Column(modifier = modifier.fillMaxSize()) {
            LineTwoTable()
            LineThirdTable(selectedDevice = selectedDevice)
            LineForthTable()
            LineFifthTable()

            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {

                Row(modifier = Modifier.fillMaxSize()) {

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
                                selectedRowIndex = selectedRowIndex,
                                onRowSelected = { selectedRowIndex = it },
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
                                            //-----------------hex----------------------------------
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
                                                        displayRows.forEachIndexed { i, row ->
                                                            Text(
                                                                text = row.hexBase,
                                                                fontSize = 12.sp,
                                                                textAlign = TextAlign.Center,
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .height(24.dp) // Совпадает с ParameterSection
                                                                    .background(if (selectedRowIndex == i) Color.Cyan.copy(alpha = 0.25f) else Color.Transparent)
                                                                    .clickable { selectedRowIndex = i }
                                                                    .padding(vertical = 4.dp),
                                                                color = row.rowColor
                                                            )
                                                        }
                                                    }
                                                }
                                            )
                                            //-------------------Physical------------------------
                                            creatorColumn(
                                                modifier = Modifier.weight(1f - hexBase),
                                                headerTitle = "Physical",
                                                headerHeight = 25.dp,
                                                headerBgColor = Color(0xFFE0E0E0),
                                                isResizable = false,
                                                dividerThickness = 0.dp,
                                                content = {
                                                    Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                        displayRows.forEachIndexed { i, row ->
                                                            Text(
                                                                text = row.physBase,
                                                                fontSize = 12.sp,
                                                                textAlign = TextAlign.Center,
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .height(24.dp)
                                                                    .background(if (selectedRowIndex == i) Color.Cyan.copy(alpha = 0.25f) else Color.Transparent)
                                                                    .clickable { selectedRowIndex = i }
                                                                    .padding(vertical = 4.dp),
                                                                color = row.rowColor
                                                            )
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                )
                                // --------------------- СТОЛБЕЦ КОНТРОЛЛЕР ------------------------
                                creatorColumn(
                                    modifier = Modifier.weight(1f - comparisonWeight),
                                    headerTitle = "КОНТРОЛЛЕР",
                                    headerHeight = 25.dp,
                                    headerBgColor = Color(0xFFE0E0E0),
                                    isResizable = false,
                                    content = {
                                        Row(modifier = Modifier.fillMaxSize()) {
                                            //-----------------hex----------------------------------
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
                                                        displayRows.forEachIndexed { i, row ->
                                                            Text(
                                                                text = row.hexCtrl,
                                                                fontSize = 12.sp,
                                                                textAlign = TextAlign.Center,
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .height(24.dp)
                                                                    .background(if (selectedRowIndex == i) Color.Cyan.copy(alpha = 0.25f) else Color.Transparent)
                                                                    .clickable { selectedRowIndex = i }
                                                                    .padding(vertical = 4.dp),
                                                                color = row.rowColor
                                                            )
                                                        }
                                                    }
                                                }
                                            )
                                            //-------------------Physical---------------------------
                                            creatorColumn(
                                                modifier = Modifier.weight(1f - hexlController),
                                                headerTitle = "Physical",
                                                headerHeight = 25.dp,
                                                headerBgColor = Color(0xFFE0E0E0),
                                                isResizable = false,
                                                dividerThickness = 0.dp,
                                                content = {
                                                    Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                        displayRows.forEachIndexed { i, row ->
                                                            Text(
                                                                text = row.physCtrl,
                                                                fontSize = 12.sp,
                                                                textAlign = TextAlign.Center,
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .height(24.dp)
                                                                    .background(if (selectedRowIndex == i) Color.Cyan.copy(alpha = 0.25f) else Color.Transparent)
                                                                    .clickable { selectedRowIndex = i }
                                                                    .padding(vertical = 4.dp),
                                                                color = row.rowColor
                                                            )
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    )
                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(tableScrollState),
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
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