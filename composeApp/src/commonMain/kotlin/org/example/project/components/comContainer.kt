//comContainer.kt

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.example.project.utils.creatorColumn

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

    var isResizing by remember { mutableStateOf(false) }//Изменяем цвет разделительной линии

    //Разделение правой половины по 5-ю строками
    var innerColumnWeight by remember { mutableStateOf(0.5f) } // Разделение 50/50 внутри правой части
    var isInnerResizing by remember { mutableStateOf(false) }  // Состояние перетаскивания для новой линии

    // Ключ - локация, Значение - список файлов
    val devicesMap = remember { mutableStateMapOf<String, MutableList<DeviceInfoIni>>() }

    val scope = rememberCoroutineScope()//Эта переменная будет хранить объект устройства, которое мы выбрали кликом
    val tableScrollState = rememberScrollState()//Создаем общий контроллер прокрутки для всех колонок с данными

    var selectedDevice by remember { mutableStateOf<DeviceInfoIni?>(null) }

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
                    // --- Секция двух столбцов ---
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
                                                    .clickable {selectedDevice = device}
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        }
                        //-------------------------------- Разделитель -----------------------------
                        Box(
                            modifier = Modifier
                                .width(TableConfig.lineThickness)
                                .fillMaxHeight()
                                // Динамическая смена цвета: если тянем — подсвечиваем, если нет — стандартный цвет
                                .background(if (isResizing) Color(0xFF0066CC) else TableConfig.lineColor)
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = { isResizing = true }, // Фиксируем начало касания
                                        onDragEnd = { isResizing = false },   // Возвращаем цвет после отпускания
                                        onDragCancel = { isResizing = false }
                                    ) { change, dragAmount ->
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
                                // --- НИЖНЯЯ ЧАСТЬ: 2 новых столбца на всю оставшуюся высоту ---
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f) // Занимает всё свободное место снизу
                                ) {
// ------------- ЛЕВЫЙ внутренний столбец "ПАРАМЕТР" (Черный текст) ----------------------
                                    creatorColumn(
                                        modifier = Modifier.weight(innerColumnWeight),
                                        headerTitle = "ПАРАМЕТР",
                                        headerHeight = 25.dp,
                                        headerBgColor = Color(0xFFDFE5CA),
                                        headerFontSize = 16,
                                        isResizable = true,
                                        dividerThickness = TableConfig.lineThickness,
                                        dividerColor = TableConfig.lineColor,
                                        dividerActiveColor = Color(0xFFC0FF00),
                                        onResize = { dragDelta ->
                                            val rightPartWidth = tableWidth.value * (1f - leftColumnWeight)
                                            val deltaWeight = dragDelta / rightPartWidth
                                            innerColumnWeight = (innerColumnWeight + deltaWeight).coerceIn(0.1f, 0.9f)
                                        },
                                        content = {
                                            var weightCol2 by remember { mutableStateOf(0.5f) }
                                            var weightCol3 by remember { mutableStateOf(0.5f) }
                                            val fixedWidth = 60.dp

                                            Row(modifier = Modifier.fillMaxSize()) {
                                                // --- 1-й СТОЛБЕЦ "№" ---
                                                creatorColumn(
                                                    modifier = Modifier.width(fixedWidth),
                                                    headerTitle = "№",
                                                    headerHeight = 25.dp,
                                                    isResizable = false,
                                                    dividerThickness = 2.dp,
                                                    content = {
                                                        Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                            selectedDevice?.flashParameters?.forEach { parameter ->
                                                                Box(
                                                                    modifier = Modifier.fillMaxWidth().height(24.dp),
                                                                    contentAlignment = Alignment.Center
                                                                ) {
                                                                    Text(
                                                                        text = parameter.code,
                                                                        fontSize = 11.sp,
                                                                        color = Color.Black // Установили черный цвет
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                )

                                                // --- 2-й СТОЛБЕЦ "Имя" ---
                                                creatorColumn(
                                                    modifier = Modifier.weight(weightCol2),
                                                    headerTitle = "Имя",
                                                    headerHeight = 25.dp,
                                                    onResize = { dragDelta ->
                                                        val delta = dragDelta / 200f
                                                        weightCol2 = (weightCol2 + delta).coerceIn(0.1f, 0.9f)
                                                        weightCol3 = (1f - weightCol2)
                                                    },
                                                    content = {
                                                        Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                            selectedDevice?.flashParameters?.forEach { parameter ->
                                                                Box(
                                                                    modifier = Modifier.fillMaxWidth().height(24.dp).padding(horizontal = 4.dp),
                                                                    contentAlignment = Alignment.CenterStart
                                                                ) {
                                                                    Text(
                                                                        text = parameter.idName,
                                                                        fontSize = 11.sp,
                                                                        maxLines = 1,
                                                                        softWrap = false,
                                                                        color = Color.Black // Установили черный цвет
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                )

                                                // --- 3-й СТОЛБЕЦ "Описание" ---
                                                creatorColumn(
                                                    modifier = Modifier.weight(weightCol3),
                                                    headerTitle = "Описание",
                                                    headerHeight = 25.dp,
                                                    onResize = { dragDelta ->
                                                        val delta = dragDelta / 200f
                                                        weightCol3 = (weightCol3 + delta).coerceIn(0.1f, 0.9f)
                                                        weightCol2 = (1f - weightCol3)
                                                    },
                                                    content = {
                                                        Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                            selectedDevice?.flashParameters?.forEach { parameter ->
                                                                Box(
                                                                    modifier = Modifier.fillMaxWidth().height(24.dp).padding(horizontal = 4.dp),
                                                                    contentAlignment = Alignment.CenterStart
                                                                ) {
                                                                    Text(
                                                                        text = parameter.description,
                                                                        fontSize = 11.sp,
                                                                        maxLines = 1,
                                                                        softWrap = false,
                                                                        color = Color.Black // Установили черный цвет
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                )

                                                // --- 4-й СТОЛБЕЦ "Ед. изм." ---
                                                creatorColumn(
                                                    modifier = Modifier.width(fixedWidth),
                                                    headerTitle = "Ед. изм.",
                                                    headerHeight = 25.dp,
                                                    isResizable = false,
                                                    dividerThickness = 0.dp,
                                                    content = {
                                                        Column(modifier = Modifier.fillMaxSize().verticalScroll(tableScrollState)) {
                                                            selectedDevice?.flashParameters?.forEach { parameter ->
                                                                Box(
                                                                    modifier = Modifier.fillMaxWidth().height(24.dp),
                                                                    contentAlignment = Alignment.Center
                                                                ) {
                                                                    Text(
                                                                        text = parameter.unit,
                                                                        fontSize = 11.sp,
                                                                        color = Color.Black // Установили черный цвет
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    )
                                    // ПРАВЫЙ внутренний столбец
                                    // --- Состояния для весов трех правых столбцов (в начале ComContainer) ---
                                    var weightRight1 by remember { mutableStateOf(1f) }
                                    var weightRight2 by remember { mutableStateOf(1f) }


                                    var weightSubColumn1 by remember { mutableStateOf(1f) }//Столбец "База"
                                    var weightSubColumn2 by remember { mutableStateOf(1f) }//Столбец "База"

                                    var weightSubColumn3 by remember { mutableStateOf(1f) }//Столбец "Контроллер"
                                    var weightSubColumn4 by remember { mutableStateOf(1f) }//Столбец "Контроллер"
                                    // ПРАВЫЙ внутренний сектор с независимыми разделителями
                                    Box(
                                        modifier = Modifier
                                            .weight(1f - innerColumnWeight)
                                            .fillMaxHeight()
                                    ) {
                                        BoxWithConstraints(modifier = Modifier.fillMaxSize().background(Color.White)) {
                                            val totalWidthPx = constraints.maxWidth.toFloat()
                                            // Важно: берем сумму текущих весов для точного расчета дельты
                                            val sumWeights = weightRight1 + weightRight2

                                            Row(modifier = Modifier.fillMaxSize()) {
                                                // ПРАВЫЙ внутренний сектор: БАЗА (Обновленный)
                                                creatorColumn(
                                                    modifier = Modifier.weight(weightRight1),
                                                    headerTitle = "База",
                                                    headerHeight = 25.dp,
                                                    headerBgColor = Color(0xFFA7A789),
                                                    headerTextColor = Color.Black,
                                                    headerFontSize = 16,
                                                    isResizable = true,
                                                    dividerThickness = TableConfig.lineThickness,
                                                    dividerColor = TableConfig.lineColor,
                                                    dividerActiveColor = Color(0xFFC0FF00),
                                                    onResize = { dragDelta ->
                                                        val delta =
                                                            (dragDelta / totalWidthPx) * sumWeights
                                                        if (weightRight2 - delta > 0.1f) {
                                                            weightRight1 =
                                                                (weightRight1 + delta).coerceAtLeast(
                                                                    0.1f
                                                                )
                                                            weightRight2 = (weightRight2 - delta)
                                                        }
                                                    },
                                                    content = {
                                                        val currentBaseWidthPx =
                                                            totalWidthPx * (weightRight1 / sumWeights)
                                                        val subSumWeights =
                                                            weightSubColumn1 + weightSubColumn2

                                                        Row(modifier = Modifier.fillMaxSize()) {
                                                            // Внутренний столбец: hex (База)
                                                            creatorColumn(
                                                                modifier = Modifier.weight(
                                                                    weightSubColumn1
                                                                ),
                                                                headerTitle = "hex",
                                                                headerHeight = 25.dp,
                                                                headerBgColor = Color(0xFFC9C9C5),
                                                                headerFontSize = 12,
                                                                isResizable = true,
                                                                dividerThickness = 2.dp,
                                                                dividerColor = TableConfig.lineColor,
                                                                dividerActiveColor = Color(
                                                                    0xFFC0FF00
                                                                ),
                                                                onResize = { dragDelta ->
                                                                    val delta =
                                                                        (dragDelta / currentBaseWidthPx) * subSumWeights
                                                                    if (weightSubColumn2 - delta > 0.1f) {
                                                                        weightSubColumn1 =
                                                                            (weightSubColumn1 + delta).coerceAtLeast(
                                                                                0.1f
                                                                            )
                                                                        weightSubColumn2 =
                                                                            (weightSubColumn2 - delta)
                                                                    }
                                                                },
                                                                content = {
                                                                    // Используем общий скролл и черный текст
                                                                    Column(
                                                                        modifier = Modifier.fillMaxSize()
                                                                            .verticalScroll(
                                                                                tableScrollState
                                                                            )
                                                                    ) {
                                                                        selectedDevice?.flashParameters?.forEach { parameter ->
                                                                            Box(
                                                                                modifier = Modifier.fillMaxWidth()
                                                                                    .height(24.dp),
                                                                                contentAlignment = Alignment.Center
                                                                            ) {
                                                                                Text(
                                                                                    text = parameter.hexBase, // Значение x0014
                                                                                    fontSize = 11.sp,
                                                                                    color = Color.Black
                                                                                )
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            )

                                                            // Внутренний столбец: Physical (База)
                                                            creatorColumn(
                                                                modifier = Modifier.weight(
                                                                    weightSubColumn2
                                                                ),
                                                                headerTitle = "Physical",
                                                                headerHeight = 25.dp,
                                                                headerBgColor = Color(0xFFC9C9C5),
                                                                headerFontSize = 12,
                                                                isResizable = false,
                                                                dividerThickness = 0.dp,
                                                                content = {
                                                                    Column(
                                                                        modifier = Modifier.fillMaxSize()
                                                                            .verticalScroll(
                                                                                tableScrollState
                                                                            )
                                                                    ) {
                                                                        selectedDevice?.flashParameters?.forEach { parameter ->
                                                                            Box(
                                                                                modifier = Modifier.fillMaxWidth()
                                                                                    .height(24.dp),
                                                                                contentAlignment = Alignment.Center
                                                                            ) {
                                                                                Text(
                                                                                    text = parameter.physBase, // Десятичное значение
                                                                                    fontSize = 11.sp,
                                                                                    color = Color.Black
                                                                                )
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            )
                                                        }
                                                    }
                                                )
                                                // Столбец Контроллер с полностью кликабельной строкой внутри
                                                // ------------- Столбец Контроллер с заполнением данных ----------------------
                                                creatorColumn(
                                                    modifier = Modifier.weight(weightRight2),
                                                    headerTitle = "Контроллер",
                                                    headerHeight = 25.dp,
                                                    headerBgColor = Color(0xFFA7A789),
                                                    headerTextColor = Color.Black,
                                                    headerFontSize = 16,
                                                    isResizable = false,
                                                    dividerThickness = 0.dp,
                                                    onResize = { /* Ресайз не требуется */ },
                                                    onHeaderClick = {
                                                        println("Нажата вся область заголовка Контроллер")
                                                    },
                                                    content = {
                                                        // Вычисляем ширину именно этого столбца (Контроллер) в пикселях для внутренних нужд
                                                        val currentBaseWidthPx =
                                                            totalWidthPx * (weightRight2 / sumWeights)
                                                        val subSumWeights =
                                                            weightSubColumn3 + weightSubColumn4

                                                        Row(modifier = Modifier.fillMaxSize()) {
                                                            // Внутренний столбец: hex
                                                            creatorColumn(
                                                                modifier = Modifier.weight(
                                                                    weightSubColumn3
                                                                ),
                                                                headerTitle = "hex",
                                                                headerHeight = 25.dp,
                                                                headerBgColor = Color(0xFFC9C9C5),
                                                                headerFontSize = 12,
                                                                isResizable = true,
                                                                dividerThickness = 2.dp,
                                                                dividerColor = TableConfig.lineColor,
                                                                dividerActiveColor = Color(
                                                                    0xFFC0FF00
                                                                ),
                                                                onResize = { dragDelta ->
                                                                    val delta =
                                                                        (dragDelta / currentBaseWidthPx) * subSumWeights
                                                                    if (weightSubColumn4 - delta > 0.1f) {
                                                                        weightSubColumn3 =
                                                                            (weightSubColumn3 + delta).coerceAtLeast(
                                                                                0.1f
                                                                            )
                                                                        weightSubColumn4 =
                                                                            (weightSubColumn4 - delta)
                                                                    }
                                                                },
                                                                content = {
                                                                    // НАПОЛНЕНИЕ: Список hex значений контроллера
                                                                    Column(
                                                                        modifier = Modifier.fillMaxSize()
                                                                            .verticalScroll(
                                                                                tableScrollState
                                                                            )
                                                                    ) {
                                                                        selectedDevice?.flashParameters?.forEach { parameter ->
                                                                            Box(
                                                                                modifier = Modifier.fillMaxWidth()
                                                                                    .height(24.dp),
                                                                                contentAlignment = Alignment.Center
                                                                            ) {
                                                                                Text(
                                                                                    text = parameter.hexCtrl, // Отображает x0000
                                                                                    fontSize = 11.sp,
                                                                                    color = Color.Black
                                                                                )
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            )
                                                            // Внутренний столбец: Physical
                                                            creatorColumn(
                                                                modifier = Modifier.weight(
                                                                    weightSubColumn4
                                                                ),
                                                                headerTitle = "Physical",
                                                                headerHeight = 25.dp,
                                                                headerBgColor = Color(0xFFC9C9C5),
                                                                headerFontSize = 12,
                                                                isResizable = false,
                                                                dividerThickness = 0.dp,
                                                                content = {
                                                                    // НАПОЛНЕНИЕ: Список Physical значений контроллера
                                                                    Column(
                                                                        modifier = Modifier.fillMaxSize()
                                                                            .verticalScroll(
                                                                                tableScrollState
                                                                            )
                                                                    ) {
                                                                        selectedDevice?.flashParameters?.forEach { parameter ->
                                                                            Box(
                                                                                modifier = Modifier.fillMaxWidth()
                                                                                    .height(24.dp),
                                                                                contentAlignment = Alignment.Center
                                                                            ) {
                                                                                Text(
                                                                                    text = parameter.physCtrl, // Отображает 0
                                                                                    fontSize = 11.sp,
                                                                                    color = Color.Black
                                                                                )
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            )
                                                        }
                                                    }
                                                )
                                                // --- Отображение окна ошибки ---
                                                if (showErrorDialog) {
                                                    androidx.compose.material3.AlertDialog(
                                                        onDismissRequest = {
                                                            showErrorDialog = false
                                                        },
                                                        title = { androidx.compose.material3.Text("Ошибка формата") },
                                                        text = {
                                                            androidx.compose.material3.Text(
                                                                errorMessage
                                                            )
                                                        },
                                                        confirmButton = {
                                                            androidx.compose.material3.TextButton(
                                                                onClick = {
                                                                    showErrorDialog = false
                                                                }) {
                                                                androidx.compose.material3.Text("ОК")
                                                            }
                                                        }
                                                    )
                                                }
                                            }}}}}}}}}}}}