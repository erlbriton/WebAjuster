package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderTable(
    thickness: Dp = TableConfig.lineThickness,
    color: Color = TableConfig.lineColor
) {
    // Состояния для меню
    var expanded by remember { mutableStateOf(false) }
    val menuItems = listOf("Обновить", "Серийные номера", "Место установки", "Тип механизма", "Дата последнего обслуживания", "Тип устройства")

    var clue by remember { mutableStateOf(false) }
    val oscilligraphItems = listOf("Открыть осциллогаф подключенного устройства", "Открыть осциллограф", "Просмотреть осциллогамму", "Новый осциллограф")

    var clueHelp by remember { mutableStateOf(false) }
    val helpItems = listOf("Ajuster Help", "About")

    // Состояния для селектора памяти
    var selectorExpanded by remember { mutableStateOf(false) }
    var selectedMemory by remember { mutableStateOf("Flash") }
    val memoryOptions = listOf("Flash", "CD", "RAM")

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = thickness, color = color)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(TableConfig.headerBackground),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- 1. КНОПКА ОБНОВИТЬ ---
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Обновить список устройств", fontSize = 12.sp) } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .clickable { expanded = true }
                            .border(1.dp, Color.Blue)
                            .background(Color.White)
                            .height(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                            Icon(Icons.Default.Build, null, modifier = Modifier.size(18.dp), tint = Color(0xFF04C104))
                        }
                        Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.Blue))
                        Box(modifier = Modifier.padding(horizontal = 2.dp)) {
                            Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(16.dp))
                        }
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        menuItems.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(text = label, fontSize = 8.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.height(16.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                onClick = { expanded = false }
                            )
                        }
                    }
                }
            }

            // 2. Поиск
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Поиск устройств в сети Modbus") } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) {
                    Row(
                        modifier = Modifier.clickable {}.border(1.dp, Color.Blue).background(Color.White).padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, null, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // 3. Отчеты
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Генератор отчетов в Exel") } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) {
                    Row(
                        modifier = Modifier.clickable {}.border(1.dp, Color.Blue).background(Color.White).padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ListAlt, null, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // --- 4. ОСЦИЛЛОГРАФ ---
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Осциллограф", fontSize = 12.sp) } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) {
                    Row(
                        modifier = Modifier
                            .clickable { clue = true }
                            .border(1.dp, Color.Blue)
                            .background(Color.White)
                            .height(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                            Icon(Icons.AutoMirrored.Filled.ShowChart, null, modifier = Modifier.size(20.dp), tint = Color.Red)
                        }
                        Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.Blue))
                        Box(modifier = Modifier.padding(horizontal = 2.dp)) {
                            Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(16.dp))
                        }
                    }
                    DropdownMenu(expanded = clue, onDismissRequest = { clue = false }) {
                        oscilligraphItems.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(text = label, fontSize = 8.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.height(16.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                onClick = { clue = false }
                            )
                        }
                    }
                }
            }

            // 5. Терминал
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Терминал") } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) {
                    Row(
                        modifier = Modifier.clickable {}.border(1.dp, Color.Blue).background(Color.White).padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Terminal, null, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // 6. Help
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Help", fontSize = 12.sp) } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) {
                    Row(
                        modifier = Modifier.clickable { clueHelp = true }.border(1.dp, Color.Blue).background(Color.White).padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.MenuBook, null, modifier = Modifier.size(16.dp))
                        Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(20.dp))
                    }
                    DropdownMenu(expanded = clueHelp, onDismissRequest = { clueHelp = false }) {
                        helpItems.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(text = label, fontSize = 8.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.height(16.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                onClick = { clueHelp = false }
                            )
                        }
                    }
                }
            }

            // 7. Файлы
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Файловые операции") } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 20.dp)) {
                    Row(
                        modifier = Modifier.clickable {}.border(1.dp, Color.Blue).background(Color.White).padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Save, null, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // 8. Черный ящик
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Черный ящик") } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) {
                    Row(
                        modifier = Modifier.clickable {}.border(1.dp, Color.Blue).background(Color.White).padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ViewInAr, null, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // --- 9. ВЫБОР ПАМЯТИ ---
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Выбор области памяти", fontSize = 12.sp) } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    Row(
                        modifier = Modifier.border(1.dp, Color.Gray).background(Color.White).height(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.padding(2.dp).background(Color(0xFF0066CC)).padding(horizontal = 4.dp), contentAlignment = Alignment.Center) {
                            Text(text = selectedMemory.uppercase(), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                        Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.Gray))
                        Box(modifier = Modifier.fillMaxHeight().width(22.dp).background(Color(0xFFE0E0E0)).clickable { selectorExpanded = true }, contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(18.dp))
                        }
                    }
                    DropdownMenu(
                        expanded = selectorExpanded,
                        onDismissRequest = { selectorExpanded = false },
                        modifier = Modifier.background(Color.White).border(1.dp, Color.Black)
                    ) {
                        memoryOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.height(14.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                onClick = { selectedMemory = option; selectorExpanded = false }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = thickness, color = color)
    }
}