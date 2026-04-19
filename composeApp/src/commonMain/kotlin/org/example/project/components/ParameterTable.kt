package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity

data class ParameterRow(
    val pn: String, val name: String, val description: String,
    val unit: String, val baseHex: String, val basePhys: String,
    val devHex: String, val devPhys: String
)

@Composable
fun SimpleDropdown(options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit, width: Int) {
    var expanded by remember { mutableStateOf(false) }
    val darkBg = Color(0xFF1A1A1A)
    val lightActionArea = Color(0xFF555555)

    Box(
        modifier = Modifier
            .width(width.dp)
            .height(30.dp)
            .background(darkBg)
            .border(1.dp, Color(0xFF777777))
            .pointerInput(Unit) { detectTapGestures { expanded = true } }
    ) {
        Text(
            text = selectedOption,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(30.dp)
                .background(lightActionArea),
            contentAlignment = Alignment.Center
        ) {
            Text("▼", fontSize = 10.sp, color = Color.White)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, fontSize = 12.sp) },
                    onClick = { onOptionSelected(option); expanded = false },
                    modifier = Modifier.height(28.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SplitMenuButton(
    text: String,
    onMainClick: () -> Unit,
    menuItems: List<String>,
    onMenuItemClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val buttonColor = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555))

    Row(
        modifier = Modifier.height(30.dp).border(1.dp, Color.Gray, RectangleShape)
    ) {
        Button(
            onClick = onMainClick,
            modifier = Modifier.fillMaxHeight(),
            shape = RectangleShape,
            colors = buttonColor,
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text(text, fontSize = 11.sp)
        }
        Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.Gray))
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxHeight().width(30.dp),
            shape = RectangleShape,
            colors = buttonColor,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("▼", fontSize = 10.sp)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            menuItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, fontSize = 12.sp) },
                    onClick = {
                        onMenuItemClick(item)
                        expanded = false
                    },
                    modifier = Modifier.height(28.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ParameterTable(parameters: List<ParameterRow>, modifier: Modifier = Modifier) {
    val bgColor = Color(0xFF1A1A1A)
    val headerBgColor = Color(0xFF2D2D2D)
    val groupHeaderBgColor = Color(0xFF3D3D3D)
    val rowBgColor = Color(0xFF222222)
    val dividerColor = Color(0xFF555555)
    val textColor = Color(0xFFE0E0E0)
    val headerTextColor = Color.White

    var leftColumnWidth by remember { mutableStateOf(100.dp) }
    val density = LocalDensity.current

    val displayParameters = remember(parameters) {
        parameters + List(60) { i ->
            ParameterRow("p${20000 + i}", "Param_$i", "Описание $i", "U", "0x00", "0.0", "0x00", "0.0")
        }
    }

    val listState = rememberLazyListState()
    val colWeights = remember { mutableStateListOf(1f, 1.5f, 2.5f, 1f, 1f, 1f, 1f, 1f) }

    val paramWeight by remember { derivedStateOf { colWeights.subList(0, 4).sum() } }
    val baseWeight by remember { derivedStateOf { colWeights.subList(4, 6).sum() } }
    val deviceWeight by remember { derivedStateOf { colWeights.subList(6, 8).sum() } }

    @Composable
    fun VerticalDivider() = Box(modifier = Modifier.fillMaxHeight().width(2.dp).background(dividerColor))

    Column(modifier = modifier.fillMaxSize().background(bgColor)) {

        Box(modifier = Modifier.fillMaxWidth().height(40.dp).background(rowBgColor)) {
            var selectedMode by remember { mutableStateOf("Flash") }

            Row(
                modifier = Modifier.fillMaxHeight().padding(start = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                SplitMenuButton("Обновить", { println("Обновление") }, listOf("Обновить", "Серийные номера", "Место установки", "Тип механизма", "Дата последнего обслуживания", "Настройки"), { println("Выбрано: $it") })
                Button(onClick = {}, modifier = Modifier.size(80.dp, 30.dp), contentPadding = PaddingValues(0.dp), shape = RectangleShape) { Text("Поиск", fontSize = 11.sp) }
                Button(onClick = {}, modifier = Modifier.size(80.dp, 30.dp), contentPadding = PaddingValues(0.dp), shape = RectangleShape) { Text("Отчет", fontSize = 11.sp) }
                SplitMenuButton("Осц", { println("Открыть осциллограф подключенного устройства") }, listOf("Открыть осциллограф подключенного устройства", "Открыть осциллограф", "Просмотр осциллограммы", "Новый осциллограф"), { println("Выбрано: $it") })
                Button(onClick = {}, modifier = Modifier.size(80.dp, 30.dp), contentPadding = PaddingValues(0.dp), shape = RectangleShape) { Text("КС", fontSize = 11.sp) }
                SplitMenuButton("Help", { println("Ajuster help") }, listOf("Ajuster help", "About"), { println("Выбрано: $it") })
                Spacer(modifier = Modifier.width(100.dp))
                Button(onClick = {}, modifier = Modifier.size(80.dp, 30.dp), contentPadding = PaddingValues(0.dp), shape = RectangleShape) { Text("ФО", fontSize = 11.sp) }
                Button(onClick = {}, modifier = Modifier.size(80.dp, 30.dp), contentPadding = PaddingValues(0.dp), shape = RectangleShape) { Text("РС", fontSize = 11.sp) }
                SimpleDropdown(listOf("Flash", "CD", "RAM"), selectedMode, { selectedMode = it }, 100)
            }
        }
        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(dividerColor))

        Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Box(modifier = Modifier.width(leftColumnWidth).fillMaxHeight())
            Box(
                modifier = Modifier.width(9.dp).fillMaxHeight().background(dividerColor).pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val deltaDp = with(density) { dragAmount.x.toDp() }
                        leftColumnWidth = (leftColumnWidth + deltaDp).coerceAtLeast(10.dp)
                    }
                }
            )
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                Column(modifier = Modifier.fillMaxWidth().background(headerBgColor)) {
                    var selectedBus by remember { mutableStateOf("MODBUS") }
                    var selectedCom by remember { mutableStateOf("") }
                    var comOptions by remember { mutableStateOf(emptyList<String>()) }
                    var selectedBps by remember { mutableStateOf("115200") }

                    Text("Настройки связи", color = Color.LightGray, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp, top = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("BUS", color = textColor, fontSize = 12.sp)
                        SimpleDropdown(listOf("MODBUS RTU", "MODBUS TCP"), selectedBus, { selectedBus = it }, 100)
                        Text("COM", color = textColor, fontSize = 12.sp)
                        SimpleDropdown(comOptions, selectedCom, { selectedCom = it }, 80)
                        Text("BPS", color = textColor, fontSize = 12.sp)
                        SimpleDropdown(listOf("115200", "57600", "38400", "28800", "19200", "14400", "9600"), selectedBps, { selectedBps = it }, 90)
                        Spacer(Modifier.weight(1f))
                        Button(onClick = {}, modifier = Modifier.height(30.dp), contentPadding = PaddingValues(horizontal = 8.dp)) { Text("Подключить", fontSize = 11.sp) }
                        Button(onClick = {}, modifier = Modifier.height(30.dp), contentPadding = PaddingValues(horizontal = 8.dp)) { Text("ID", fontSize = 11.sp) }
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("ID", color = textColor, fontSize = 12.sp, modifier = Modifier.width(110.dp))
                        TextField(value = "xxxxx771 DExS.SMFCB v1.10.5.0 21.05.2022 www.intmash.ru", onValueChange = {}, modifier = Modifier.weight(1f).height(30.dp).border(1.dp, dividerColor), colors = TextFieldDefaults.colors(focusedContainerColor = bgColor, unfocusedContainerColor = bgColor))
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Тип механизм", color = textColor, fontSize = 12.sp, modifier = Modifier.width(110.dp))
                        TextField(value = "", onValueChange = {}, modifier = Modifier.width(225.dp).height(30.dp).border(1.dp, dividerColor), colors = TextFieldDefaults.colors(focusedContainerColor = bgColor, unfocusedContainerColor = bgColor))
                        Spacer(Modifier.width(8.dp))
                        TextField(value = "21.05.2022", onValueChange = {}, modifier = Modifier.width(100.dp).height(30.dp).border(1.dp, dividerColor), colors = TextFieldDefaults.colors(focusedContainerColor = bgColor, unfocusedContainerColor = bgColor))
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {}, shape = RectangleShape, modifier = Modifier.height(30.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555))) { Text("Сейчас", fontSize = 12.sp) }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {}, shape = RectangleShape, modifier = Modifier.height(30.dp).width(155.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555), contentColor = Color.White)) { Text("Клон", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Место установки", color = textColor, fontSize = 12.sp, modifier = Modifier.width(110.dp))
                        TextField(value = "БКО", onValueChange = {}, modifier = Modifier.width(225.dp).height(30.dp).border(1.dp, dividerColor), colors = TextFieldDefaults.colors(focusedContainerColor = bgColor, unfocusedContainerColor = bgColor))
                        Spacer(Modifier.width(116.dp))
                        Button(onClick = {}, shape = RectangleShape, modifier = Modifier.height(30.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555), contentColor = Color.White)) { Text("Шкалы", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        Spacer(Modifier.weight(1f))
                        Button(onClick = {}, shape = RectangleShape, modifier = Modifier.height(30.dp).width(155.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555), contentColor = Color.White)) { Text("Сохранить изменения", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(dividerColor))
                Column(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    Row(modifier = Modifier.fillMaxWidth().height(30.dp).background(groupHeaderBgColor)) {
                        Box(Modifier.weight(paramWeight).fillMaxHeight().pointerInput(Unit) {
                            detectDragGestures { change, drag ->
                                change.consume()
                                val factor = drag.x / 200f
                                colWeights[3] = (colWeights[3] + factor).coerceAtLeast(0.1f)
                                colWeights[4] = (colWeights[4] - factor).coerceAtLeast(0.1f)
                            }
                        }, Alignment.Center) { Text("Параметр", color = headerTextColor, fontWeight = FontWeight.Bold) }
                        VerticalDivider()
                        Box(Modifier.weight(baseWeight).fillMaxHeight().pointerInput(Unit) {
                            detectDragGestures { change, drag ->
                                change.consume()
                                val factor = drag.x / 200f
                                colWeights[5] = (colWeights[5] + factor).coerceAtLeast(0.1f)
                                colWeights[6] = (colWeights[6] - factor).coerceAtLeast(0.1f)
                            }
                        }, Alignment.Center) { Text("База", color = headerTextColor, fontWeight = FontWeight.Bold) }
                        VerticalDivider()
                        Box(Modifier.weight(deviceWeight).fillMaxHeight(), Alignment.Center) { Text("Контроллер", color = headerTextColor, fontWeight = FontWeight.Bold) }
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(dividerColor))
                    Row(modifier = Modifier.fillMaxWidth().height(25.dp).background(headerBgColor)) {
                        val headers = listOf("PN", "Имя", "Описание", "Ед. изм.", "hex", "Физич.", "hex", "Физич.")
                        headers.forEachIndexed { i, text ->
                            Box(Modifier.weight(colWeights[i]).fillMaxHeight().pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    val delta = dragAmount.x / 200f
                                    if (i < colWeights.size - 1) {
                                        colWeights[i] = (colWeights[i] + delta).coerceAtLeast(0.05f)
                                        colWeights[i + 1] = (colWeights[i + 1] - delta).coerceAtLeast(0.05f)
                                    }
                                }
                            }, Alignment.Center) { Text(text, color = headerTextColor, fontSize = 11.sp, textAlign = TextAlign.Center) }
                            if (i < 7) VerticalDivider()
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(dividerColor))
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                        items(displayParameters) { p ->
                            Row(modifier = Modifier.fillMaxWidth().height(25.dp).background(rowBgColor), verticalAlignment = Alignment.CenterVertically) {
                                val cells = listOf(p.pn, p.name, p.description, p.unit, p.baseHex, p.basePhys, p.devHex, p.devPhys)
                                cells.forEachIndexed { i, text ->
                                    Box(Modifier.weight(colWeights[i]).fillMaxHeight(), Alignment.Center) {
                                        Text(text, color = textColor, fontSize = 12.sp, textAlign = TextAlign.Center)
                                    }
                                    if (i < 7) VerticalDivider()
                                }
                            }
                        }
                    }
                    VerticalScrollbar(modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(), adapter = rememberScrollbarAdapter(scrollState = listState))
                }
            }
        }
    }
}