//comContainer.kt

package org.example.project.components.comcontainer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.actionsButton.HeaderActionsButtons
import org.example.project.models.DeviceInfoIni
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import org.example.project.components.HeaderTable
import org.example.project.components.LineFifthTable
import org.example.project.components.LineForthTable
import org.example.project.components.LineThirdTable
import org.example.project.components.LineTwoTable
import org.example.project.components.TableConfig
import org.example.project.utils.creatorColumn
import org.example.project.viewmodel.LocalMainViewModel
import org.example.project.viewmodel.MainViewModel

@Composable
fun ComContainer() {
    var tableWidth by remember { mutableStateOf(800.dp) }
    var leftColumnWeight by remember { mutableStateOf(0.25f) }
    val lineThickness = 2.dp
    val lineColor = Color.Gray

    var errorMessage by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var isResizing by remember { mutableStateOf(false) }

    var innerColumnWeight by remember { mutableStateOf(0.5f) }
    var isInnerResizing by remember { mutableStateOf(false) }

    val devicesMap = remember { mutableStateMapOf<String, MutableList<DeviceInfoIni>>() }
    val scope = rememberCoroutineScope()
    val tableScrollState = rememberScrollState()

    var selectedDevice by remember { mutableStateOf<DeviceInfoIni?>(null) }
    val mainViewModel = LocalMainViewModel.current

    val headerActions = remember(scope) {
        HeaderActionsButtons(
            mainViewModel = mainViewModel,
            scope = scope,
            onDeviceLoaded = { info ->
                val iterator = devicesMap.entries.iterator()
                while (iterator.hasNext()) {
                    val entry = iterator.next()
                    entry.value.removeAll { it.fileName == info.fileName }
                    if (entry.value.isEmpty()) iterator.remove()
                }
                devicesMap.getOrPut(info.location) { mutableStateListOf() }.add(info)
            },
            ShowError = { message ->
                errorMessage = message
                showErrorDialog = true
            }
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxAllowedWidth = maxWidth

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
            Row(modifier = Modifier.wrapContentWidth().fillMaxHeight()) {

                // Ручка изменения размера всей таблицы
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .height(34.dp)
                        .background(Color.Green.copy(alpha = 0.5f))
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                tableWidth -= dragAmount.x.toDp()
                                tableWidth = tableWidth.coerceIn(150.dp, maxAllowedWidth)
                            }
                        }
                )

                Column(
                    modifier = Modifier
                        .width(tableWidth)
                        .fillMaxHeight()
                        .border(width = TableConfig.lineThickness, color = TableConfig.lineColor)
                ) {
                    HeaderTable(actions = headerActions)

                    // ГЛАВНЫЙ РЯД: Лево + Разделитель + Право
                    Row(modifier = Modifier.fillMaxWidth().weight(1f)) {

                        // 1. ЛЕВЫЙ СТОЛБЕЦ (Список ID)
                        Column(
                            modifier = Modifier
                                .weight(leftColumnWeight)
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState())
                                .padding(4.dp)
                        ) {
                            val expandedGroups = remember { mutableStateListOf<String>() }

                            devicesMap.forEach { (location, devices) ->
                                val isExpanded = expandedGroups.contains(location)
                                val groupName = if (location.isEmpty()) "Unknown" else location

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (isExpanded) expandedGroups.remove(location)
                                            else expandedGroups.add(location)
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp).padding(end = 4.dp)
                                    )
                                    Text(text = groupName, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                                        Text(text = devices.size.toString(), fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp))
                                    }
                                }

                                if (isExpanded) {
                                    devices.forEach { device ->
                                        val isSelected = mainViewModel.selectedDeviceId == device.id
                                        Text(
                                            text = device.id,
                                            fontSize = 12.sp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 24.dp, end = 4.dp)
                                                .background(if (isSelected) Color(0xFFCCE5FF) else Color.Transparent, RoundedCornerShape(2.dp))
                                                .clickable {
                                                    mainViewModel.selectedDeviceId = device.id
                                                    mainViewModel.typeMechanism = device.Description ?: ""
                                                    selectedDevice = device
                                                }
                                                .padding(vertical = 2.dp, horizontal = 4.dp),
                                            maxLines = 1, overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        // 2. РАЗДЕЛИТЕЛЬНАЯ ЛИНИЯ (между лево и право)
                        Box(
                            modifier = Modifier
                                .width(TableConfig.lineThickness)
                                .fillMaxHeight()
                                .background(if (isResizing) Color(0xFF0066CC) else TableConfig.lineColor)
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = { isResizing = true },
                                        onDragEnd = { isResizing = false },
                                        onDragCancel = { isResizing = false }
                                    ) { change, dragAmount ->
                                        change.consume()
                                        val delta = dragAmount.x / tableWidth.value
                                        leftColumnWeight = (leftColumnWeight + delta).coerceIn(0.1f, 0.9f)
                                    }
                                }
                        )

                        // 3. ПРАВЫЙ СТОЛБЕЦ (Данные устройства)
                        Box(
                            modifier = Modifier
                                .weight(1f - leftColumnWeight)
                                .fillMaxHeight()
                        ) {
                            DeviceDataTable(
                                selectedDevice = selectedDevice,
                                tableScrollState = tableScrollState,
                                innerColumnWeight = innerColumnWeight,
                                onInnerResize = { dragDelta ->
                                    val rightPartWidth = tableWidth.value * (1f - leftColumnWeight)
                                    val deltaWeight = dragDelta / rightPartWidth
                                    innerColumnWeight = (innerColumnWeight + deltaWeight).coerceIn(0.1f, 0.9f)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
