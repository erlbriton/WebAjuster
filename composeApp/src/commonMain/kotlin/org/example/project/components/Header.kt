package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build // или Settings
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.TooltipAnchorPosition

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
                                text = { Text(text = label, fontSize = 12.sp) },
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
