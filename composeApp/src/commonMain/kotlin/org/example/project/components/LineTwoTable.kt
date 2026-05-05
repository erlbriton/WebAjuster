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
    var columnWidth by remember { mutableStateOf(250.dp) }
    var selectorExpanded by remember { mutableStateOf(false) }
    var selectedProtocol by remember { mutableStateOf("Modbus RTU") }
    val protocolOptions = listOf("Modbus RTU", "Modbus TCP")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(TableConfig.TwoBackground),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. ЛЕВАЯ ЧАСТЬ
            Box(
                modifier = Modifier
                    .width(columnWidth)
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text(
                        text = "Настройки связи",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall
                    )

                    // Группируем BUS и Селектор в одну горизонтальную строку
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "BUS",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(end = 8.dp) // Отступ перед селектором
                        )

                        // Селектор теперь здесь
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
                                        .background(Color(0xFF0066CC))
                                        .padding(horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = selectedProtocol.uppercase(),
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.Gray))
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(22.dp)
                                        .background(Color(0xFFE0E0E0))
                                        .clickable { selectorExpanded = true },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(18.dp))
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
                }
            }

            // 2. ПРАВАЯ ЧАСТЬ (для остальных кнопок)
            Row(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Твои кнопки будут здесь
            }
        }
    }
}