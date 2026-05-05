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
import androidx.compose.material.icons.filled.FolderOpen
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
import org.example.project.actions.HeaderActions
import org.example.project.utils.UniversalMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderTable(
    actions: HeaderActions, // 2. Добавьте этот параметр первым
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

    //Состояние для выбора папки/файла
    var selectFile by remember { mutableStateOf(false) }
    val selectOptions = listOf("Файл", "Папка")

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
                            .border(1.dp, Color.Blue)
                            .background(Color.White)
                            .height(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // ЛЕВАЯ ЧАСТЬ: Действие по умолчанию (Обновить)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable {
                                    /* ЗДЕСЬ ДЕЙСТВИЕ ПО УМОЛЧАНИЮ (например, первый пункт меню) */
                                    println("Выполнено: ${menuItems[0]}")
                                }
                                .padding(horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                null,
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFF04C104)
                            )
                        }

                        // РАЗДЕЛИТЕЛЬ
                        Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.Blue))

                        // ПРАВАЯ ЧАСТЬ: Открытие меню
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable { expanded = true } // Только эта часть открывает меню
                                .padding(horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Меню (появляется под всей кнопкой)
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        menuItems.forEach { label ->
                            // Используем нашу обертку
                            UniversalMenuItem(
                                label = label,
                                itemHeight = 16.dp, // Задаем высоту здесь
                                onClick = {
                                    expanded = false
                                    /* Ваше действие */
                                }
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
                            .border(1.dp, Color.Blue)
                            .background(Color.White)
                            .height(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // ЛЕВАЯ ЧАСТЬ: Основное действие (например, открыть первый пункт списка)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable {
                                    /* ЗДЕСЬ ДЕЙСТВИЕ ПО УМОЛЧАНИЮ */
                                    println("Выполнено: ${oscilligraphItems[0]}")
                                }
                                .padding(horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ShowChart,
                                null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.Red
                            )
                        }

                        // РАЗДЕЛИТЕЛЬ
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(Color.Blue)
                        )

                        // ПРАВАЯ ЧАСТЬ: Открытие меню со списком опций
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable { clue = true }
                                .padding(horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Выпадающее меню под всей кнопкой
                    DropdownMenu(
                        expanded = clue,
                        onDismissRequest = { clue = false },
                        // Устанавливаем минимальную ширину, которой хватит для "Осциллографа",
                        // и позволяем расти до максимума
                        modifier = Modifier.widthIn(min = 300.dp, max = 600.dp)
                    ) {
                        oscilligraphItems.forEach { label ->
                            UniversalMenuItem(
                                label = label,
                                itemHeight = 16.dp,
                                onClick = {
                                    clue = false
                                    // Логика выбора конкретной опции
                                }
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
                            UniversalMenuItem(
                                label = label,
                                itemHeight = 16.dp, // Задаем высоту здесь
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
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.dp, Color.Black)
                        // Если вдруг ширина снова "поплывет", добавьте сюда .widthIn(min = ...)
                    ) {
                        memoryOptions.forEach { option ->
                            UniversalMenuItem(
                                label = option,
                                itemHeight = 14.dp,
                                onClick = {
                                    selectedMemory = option      // Сохраняем выбор
                                    selectorExpanded = false     // Закрываем меню
                                }
                            )
                        }
                    }
                }
            }
            // 10. Выбор папки/файла
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Выбор файла", fontSize = 12.sp) } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) {
                    Row(
                        modifier = Modifier
                            .border(1.dp, Color.Blue)
                            .background(Color.White)
                            .height(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // ЛЕВАЯ ЧАСТЬ: Основное действие (выбор файла по клику на иконку)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable {
                                    actions.onPickFileRequest()
                                }
                                .padding(horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF046308)
                            )
                        }

                        // РАЗДЕЛИТЕЛЬ
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(Color.Blue)
                        )

                        // ПРАВАЯ ЧАСТЬ: Стрелочка для меню
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable { selectFile = true }
                                .padding(horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Выпадающее меню
                    DropdownMenu(expanded = selectFile, onDismissRequest = { selectFile = false }) {
                        // Пункт выбора Файла
                        UniversalMenuItem(
                            label = "Файл",
                            itemHeight = 16.dp,
                            onClick = {
                                selectFile = false // ОБЯЗАТЕЛЬНО ПЕРВЫМ
                                actions.onPickFileRequest()
                            }
                        )

                        // Пункт выбора Папки
                        UniversalMenuItem(
                            label = "Папка",
                            itemHeight = 16.dp,
                            onClick = {
                                selectFile = false // ОБЯЗАТЕЛЬНО ПЕРВЫМ
                                actions.onPickDirectoryRequest()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = thickness, color = color)
    }
}