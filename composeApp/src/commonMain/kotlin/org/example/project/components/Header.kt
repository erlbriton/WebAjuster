package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    // Состояние для меню
    var expanded by remember { mutableStateOf(false) }
    val menuItems = listOf(
        "Обновить",
        "Серийные номера",
        "Место установки",
        "Тип механизма",
        "Дата последнего обслуживания",
        "Тип устройства"
    )
    var clue by remember { mutableStateOf(false) }
    // 1. Состояние для управления подсказкой
    val tooltipState = rememberTooltipState()

    val oscilligraphItems = listOf(
        "Открыть осциллогаф подключенного устройства",
        "Открыть осциллограф",
        "Просмотреть осциллогамму",
        "Новый осциллограф"
    )
    var clueHelp by remember { mutableStateOf(false) }
    val helpItems = listOf(
        "Ajuster Help",
        "About"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // Верхняя граница
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = thickness,
            color = color
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(TableConfig.headerBackground),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- КНОПКА ВЫБОРА СЛЕВА ---
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    TooltipAnchorPosition.Above // Передаем просто значение
                ),
                tooltip = {
                    PlainTooltip {
                        Text("Обновить список устройств", fontSize = 12.sp)
                    }
                },
                state = tooltipState
            ) {
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    // Имитация кнопки в стиле Windows
                    Row(
                        modifier = Modifier
                            .clickable { expanded = true }
                            .border(1.dp, Color.Blue)//Цвет бордюра кнопки
                            //   .background(Color(0xFFE0E0E0))
                            .background(Color.White)//Цвет кнопки
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Иконка (зеленая, как на скрине)
                        Icon(
                            imageVector = Icons.Default.Build, // Можно заменить на свою иконку
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF04C104)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Выпадающий список
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        menuItems.forEach { label ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = label,
                                        fontSize = 8.sp,
                                        style = androidx.compose.ui.text.TextStyle(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                },
                                // Уменьшаем высоту пункта, как в 4-й кнопке
                                modifier = Modifier.height(16.dp),
                                // Убираем вертикальные отступы для компактности
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                onClick = {
                                    expanded = false
                                    // Действие при выборе
                                }
                            )
                        }
                    }
                }
            }
            // 2. Вторая кнопка (Лупа)
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    positioning = TooltipAnchorPosition.Above
                ),
                tooltip = {
                    PlainTooltip {
                        Text("Поиск устройств в сети Modbus")
                    }
                },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) { // Небольшой отступ между кнопками
                    Row(
                        modifier = Modifier
                            .clickable { /* Ваше действие поиска */ }
                            .border(1.dp, Color.Blue) // Тот же стиль, что и у первой кнопки
                            . background(Color.White)//Цвет кнопки
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search, // Иконка лупы
                            contentDescription = "Search",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black
                        )
                    }
                }
            }
            // 3. Третья кнопка
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    positioning = TooltipAnchorPosition.Above
                ),
                tooltip = {
                    PlainTooltip {
                        Text("Генератор отчетов в Exel")
                    }
                },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) { // Небольшой отступ между кнопками
                    Row(
                        modifier = Modifier
                            .clickable { /* Ваше действие поиска */ }
                            .border(1.dp, Color.Blue) // Тот же стиль, что и у первой кнопки
                            . background(Color.White)//Цвет кнопки
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ListAlt, //
                            contentDescription = "Search",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black
                        )
                    }
                }
            }

            // ---Четвертая  Кнопка осциллограф---
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Осциллограф", fontSize = 12.sp) } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) {
                    Row(
                        modifier = Modifier
                            .clickable { clue = true } // Открываем меню осциллографа
                            .border(1.dp, Color.Blue)
                            .background(Color.White)
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Иконка осциллографа (используем ShowChart как график)
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ShowChart,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Red // Сделаем красным для отличия
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Выпадающий список для осциллографа
                    DropdownMenu(
                        expanded = clue,
                        onDismissRequest = { clue = false }
                    ) {
                        oscilligraphItems.forEach { label ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = label,
                                        fontSize = 8.sp,
                                        style = androidx.compose.ui.text.TextStyle(
                                            fontWeight = FontWeight.Bold
                                    )
                                    )
                                },
                                // Уменьшаем высоту самого пункта меню
                                modifier = Modifier.height(16.dp),
                                // Убираем вертикальные отступы внутри пункта
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                onClick = {
                                    clue = false
                                    // Здесь логика выбора для осциллографа
                                }
                            )
                        }
                    }
                }
            }

            // Пятая кнопка "Командная строка"
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    positioning = TooltipAnchorPosition.Above
                ),
                tooltip = {
                    PlainTooltip {
                        Text("Терминал")
                    }
                },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) { // Небольшой отступ между кнопками
                    Row(
                        modifier = Modifier
                            .clickable { /* Ваше действие поиска */ }
                            .border(1.dp, Color.Blue) // Тот же стиль, что и у первой кнопки
                            . background(Color.White)//Цвет кнопки
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Terminal, // Иконка лупы
                            contentDescription = "Terminal",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black
                        )
                    }
                }
            }
            // ---Шестая Кнопка - Help---
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Help", fontSize = 12.sp) } },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 4.dp)) {
                    Row(
                        modifier = Modifier
                            .clickable { clueHelp = true } // Открываем меню Help
                            .border(1.dp, Color.Blue)
                            .background(Color.White)
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Иконка книги (Help)
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )

                        // Добавляем стрелочку выпадающего меню
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp) // Стрелочку обычно делают чуть крупнее иконок
                        )
                    }

                    // Выпадающий список для Help
                    DropdownMenu(
                        expanded = clueHelp,
                        onDismissRequest = { clueHelp = false }
                    ) {
                        helpItems.forEach { label ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = label,
                                        fontSize = 8.sp,
                                        style = androidx.compose.ui.text.TextStyle(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                },
                                modifier = Modifier.height(16.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                onClick = {
                                    clueHelp = false
                                }
                            )
                        }
                    }
                }
            }

            // Шестая кнопка "Файловые операции"
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    positioning = TooltipAnchorPosition.Above
                ),
                tooltip = {
                    PlainTooltip {
                        Text("Файловые операции")
                    }
                },
                state = rememberTooltipState()
            ) {
                Box(modifier = Modifier.padding(start = 20.dp)) { // Небольшой отступ между кнопками
                    Row(
                        modifier = Modifier
                            .clickable { /* Ваше действие поиска */ }
                            .border(1.dp, Color.Blue) // Тот же стиль, что и у первой кнопки
                            . background(Color.White)//Цвет кнопки
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save, // Иконка лупы
                            contentDescription = "File",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black
                        )
                    }
                }
            }

            // Этот Spacer "отталкивает" всё, что идет дальше, в правую сторону
            Spacer(modifier = Modifier.weight(1f))
            // Здесь твои остальные кнопки (прижаты вправо)
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Твои кнопки из старого кода...
            }
        }
        // Нижняя граница заголовка
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = thickness,
            color = color
        )
    }
}
