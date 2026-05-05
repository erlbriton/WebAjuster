package org.example.project.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ComContainer() {
    var tableWidth by remember { mutableStateOf(800.dp) }//Начальный размер всей таблицы
    var leftColumnWeight by remember { mutableStateOf(0.25f) }//Начальный размер левого столбца
    val scrollState = rememberScrollState()
    val lineThickness = 2.dp // Толщина всех линий
    val lineColor = Color.Gray // Цвет всех линий

    // --- Переменные для окна ошибки ---
    var errorMessage by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var deviceLocation by remember { mutableStateOf("—") }//Путь

    // Ключ - локация, Значение - список файлов
    val devicesMap = remember { mutableStateMapOf<String, MutableList<DeviceInfoIni>>() }

    val scope = rememberCoroutineScope()
    val headerActions = remember(scope) {
        HeaderActionsButtons(
            scope = scope,
            onDeviceLoaded = { info ->
                // 1. Ищем и удаляем старую копию файла с таким же именем (если она есть)
                val iterator = devicesMap.entries.iterator()
                while (iterator.hasNext()) {
                    val entry = iterator.next()
                    entry.value.removeAll { it.fileName == info.fileName }
                    if (entry.value.isEmpty()) iterator.remove() // Удаляем пустую группу
                }

                // 2. Добавляем новый файл в группу по его Location
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

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            Row(modifier = Modifier.wrapContentWidth().fillMaxHeight()) {

                // --- Ручка изменения размера ---
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .height(34.dp)
                        .background(Color.Red.copy(alpha = 0.5f))
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                tableWidth -= dragAmount.x.toDp()
                                tableWidth = tableWidth.coerceIn(150.dp, maxAllowedWidth)
                            }
                        }
                )
                // --- Сама Таблица ---
                Column(
                    modifier = Modifier
                        .width(tableWidth)
                        .fillMaxHeight() // Тянем на всю высоту
                        .border(width = TableConfig.lineThickness, color = TableConfig.lineColor)
                ) {
                    HeaderTable(actions = headerActions)
                    // LineTwoTable()
                    // --- Секция двух столбцов ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        // Левый столбец
                        // Левый столбец
                        Box(
                            modifier = Modifier
                                .weight(leftColumnWeight)
                                .fillMaxHeight()
                        ) {
                            // Храним список имен локаций, которые сейчас развернуты
                            val expandedGroups = remember { mutableStateListOf<String>() }

                            Column(
                                modifier = Modifier.fillMaxSize().padding(4.dp)
                            ) {
                                devicesMap.forEach { (location, devices) ->
                                    val isExpanded = expandedGroups.contains(location)
                                    val groupName = if (location.isEmpty()) "Unknown" else location

                                    // Строка заголовка группы
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                if (isExpanded) expandedGroups.remove(location)
                                                else expandedGroups.add(location)
                                            }
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                    ) {
                                        //Шеврон(стрелочка вниз или вправо)
                                        Icon(
                                            imageVector = if (isExpanded)
                                                androidx.compose.material.icons.Icons.Default.KeyboardArrowDown
                                            else
                                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            contentDescription = null,
                                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .size(20.dp) // Четкий размер иконки
                                                .padding(end = 4.dp)
                                        )

                                        // Название группы (location)
                                        Text(
                                            text = groupName,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1, // ЗАПРЕТ ПЕРЕНОСА
                                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis, // ОБРЕЗКА ТРОЕТОЧИЕМ
                                            modifier = Modifier.weight(1f)
                                        )

                                        // Бейдж с количеством файлов
                                        Surface(
                                            shape = androidx.compose.foundation.shape.CircleShape,
                                            color = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        ) {
                                            Text(
                                                text = devices.size.toString(),
                                                fontSize = 9.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                                                color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }

                                    // Если группа развернута — показываем список ID
                                    if (isExpanded) {
                                        devices.forEach { device ->
                                            Text(
                                                text = device.id,
                                                fontSize = 12.sp,
                                                lineHeight = 12.sp,
                                                maxLines = 1,
                                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(start = 20.dp, bottom = 4.dp, end = 4.dp) // Увеличили отступ слева под большой шеврон
                                                    .clickable { println("Выбран ID: ${device.id}") }
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        }
// Разделитель
                        Box(
                            modifier = Modifier
                                .width(TableConfig.lineThickness)
                                .fillMaxHeight()
                                .background(TableConfig.lineColor)
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
                            Column(modifier = Modifier.fillMaxSize()) {
                                LineTwoTable()//Вторая строка
                                LineThirdTable()//Третья строка
                                LineForthTable()//Четвертая строка
                                LineFifthTable()//Пятая строка
                            }
                        }
                    }
                }
            }
        }
    }
    // --- Отображение окна ошибки ---
    if (showErrorDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { androidx.compose.material3.Text("Ошибка формата") },
            text = { androidx.compose.material3.Text(errorMessage) },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = { showErrorDialog = false }) {
                    androidx.compose.material3.Text("ОК")
                }
            }
        )
    }
}