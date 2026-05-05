package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.example.project.utils.UniversalMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineTwoTable(
    thickness: Dp = TableConfig.lineThickness,
    color: Color = TableConfig.lineColor
) {
    var selectorExpanded by remember { mutableStateOf(false) }
    var selectedProtocol by remember { mutableStateOf("Modbus RTU") }
    val protocolOptions = listOf("Modbus RTU", "Modbus TCP")

    // 1. Состояния для COM-порта
    var comExpanded by remember { mutableStateOf(false) }
    var selectedCom by remember { mutableStateOf("") } // Изначально пусто

    // Список опций (пустой до подключения).
    // Когда порты найдутся, вы просто обновите этот список, например: listOf("COM3", "COM10", "COM99")
    var comOptions by remember { mutableStateOf(listOf<String>()) }

    var selectorSpeed by remember { mutableStateOf(false) }
    var chosenSpeed by remember { mutableStateOf("115200") }
    val speedOptions =
        listOf("921600", "460800", "230400", "115200", "57600", "38400", "19200", "9600")


    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(TableConfig.TwoBackground),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ЕДИНЫЙ КОНТЕЙНЕР ДЛЯ ВСЕХ ЭЛЕМЕНТОВ
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Настройки связи",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Группа BUS
                    Text(
                        text = "BUS",
                        color = Color.White,
                        fontSize = 10.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // СЕЛЕКТОР ПРОТОКОЛА
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                            TooltipAnchorPosition.Above
                        ),
                        tooltip = { PlainTooltip { Text("Протокол передачи", fontSize = 12.sp) } },
                        state = rememberTooltipState()
                    ) {
                        Box {
                            Row(
                                modifier = Modifier
                                    .border(1.dp, Color.Gray)
                                    .background(Color.White)
                                    .height(24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .background(Color(0xFF83D0D0))
                                        .padding(horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = selectedProtocol.uppercase(),
                                        color = Color.Black,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.fillMaxHeight().width(1.dp)
                                        .background(Color.Gray)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(22.dp)
                                        .background(Color(0xFF9E9393))
                                        .clickable { selectorExpanded = true },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = selectorExpanded,
                                onDismissRequest = { selectorExpanded = false },
                                modifier = Modifier
                                    .background(Color.White)
                                    .border(1.dp, Color.Black)
                                    .widthIn(min = 120.dp)
                            ) {
                                protocolOptions.forEach { option ->
                                    UniversalMenuItem(
                                        label = option,
                                        itemHeight = 14.dp,
                                        onClick = {
                                            selectedProtocol = option
                                            selectorExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // ТЕПЕРЬ COM ИДЕТ СРАЗУ СЛЕДОМ

                    Spacer(modifier = Modifier.width(5.dp)) // Пробел между BUS и COM

                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                            TooltipAnchorPosition.Above
                        ),
                        tooltip = { PlainTooltip { Text("Выбор COM порта", fontSize = 12.sp) } },
                        state = rememberTooltipState()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "COM",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            Box {
                                Row(
                                    modifier = Modifier
                                        .border(1.dp, Color.Gray)
                                        .background(Color.White)
                                        .height(24.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Текстовая область порта
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .background(Color(0xFF00B4CC))
                                            .padding(horizontal = 4.dp)
                                            .widthIn(min = 36.dp), // Гарантирует ширину примерно в 4 символа, даже если текста нет
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = selectedCom.uppercase(),
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            maxLines = 1
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.fillMaxHeight().width(1.dp)
                                            .background(Color.Gray)
                                    )

                                    // Кнопка выпадающего списка
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(22.dp)
                                            .background(Color(0xFFE0E0E0))
                                            .clickable {
                                                // Меню открывается только если есть доступные порты
                                                if (comOptions.isNotEmpty()) comExpanded = true
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp),
                                            // Делаем иконку бледной, если список пуст
                                            tint = if (comOptions.isNotEmpty()) Color.Black else Color.Gray
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = comExpanded,
                                    onDismissRequest = { comExpanded = false },
                                    modifier = Modifier
                                        .background(Color.White)
                                        .border(1.dp, Color.Black)
                                        .widthIn(min = 80.dp) // Ширина самого выпадающего списка
                                ) {
                                    comOptions.forEach { option ->
                                        UniversalMenuItem(
                                            label = option,
                                            itemHeight = 20.dp, // Немного увеличил высоту для удобства клика
                                            onClick = {
                                                selectedCom = option
                                                comExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(5.dp)) // Пробел
                    //Скорость передачи
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                            TooltipAnchorPosition.Above
                        ),
                        tooltip = {
                            PlainTooltip {
                                Text(
                                    "Скорость передачи [bps]",
                                    fontSize = 12.sp
                                )
                            }
                        },
                        state = rememberTooltipState()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "BPS",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Box {
                                Row(
                                    modifier = Modifier
                                        .border(1.dp, Color.Gray)
                                        .background(Color.White)
                                        .height(24.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .background(Color(0xFF83D0D0))
                                            .padding(horizontal = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = chosenSpeed.uppercase(),
                                            color = Color.Black,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                    Spacer(
                                        modifier = Modifier.fillMaxHeight().width(1.dp)
                                            .background(Color.Gray)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(22.dp)
                                            .background(Color(0xFF9E9393))
                                            .clickable { selectorSpeed = true },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = selectorSpeed,
                                    onDismissRequest = { selectorSpeed = false },
                                    modifier = Modifier
                                        .background(Color.White)
                                        .border(1.dp, Color.Black)
                                        .widthIn(min = 120.dp)
                                ) {
                                    speedOptions.forEach { option ->
                                        UniversalMenuItem(
                                            label = option,
                                            itemHeight = 14.dp,
                                            onClick = {
                                                chosenSpeed = option
                                                selectorSpeed = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Если нужно прижать будущие кнопки в самый конец (вправо)
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}