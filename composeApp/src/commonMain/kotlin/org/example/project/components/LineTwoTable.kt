package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.sp

@Composable
fun LineTwoTable(
    thickness: Dp = TableConfig.lineThickness,
    color: Color = TableConfig.lineColor
) {
    // Состояние для управления шириной левой части
    var columnWidth by remember { mutableStateOf(250.dp) }

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

            // 1. ЛЕВАЯ ЧАСТЬ (Контейнер с текстом)
            Box(
                modifier = Modifier
                    .width(columnWidth)
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(
                        text = "Настройки связи",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "BUS",
                        color = Color.White,
                        fontSize = 16.sp // Чуть меньше основного текста
                    )
                }
            }

            // 2. ПРАВАЯ ЧАСТЬ (Остальное пространство для кнопок)
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