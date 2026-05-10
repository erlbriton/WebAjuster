package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LineFifthTable(thickness: Dp = TableConfig.lineThickness, // Используем значение по умолчанию из конфига
                   color: Color = TableConfig.lineColor) {
    // Column служит контейнером, который выстраивает элементы вертикально.
    Column(
        modifier = Modifier.fillMaxWidth(), // Растягиваем контейнер на всю ширину экрана.
        horizontalAlignment = Alignment.End // ПРИЖИМАЕМ ВСЕ элементы (Row и Divider) вправо.
    ) {
        // Row - это сама строка с данными/кнопками третьей строки.
        Row(
            modifier = Modifier
                .fillMaxWidth() // Занимает ровно 33% ширины родителя (как и первая строка).
                .height(25.dp)       // Фиксированная высота для единообразия.
                .background(TableConfig.headerBackground), // Цвет фона.
            verticalAlignment = Alignment.CenterVertically // Выравнивание кнопок внутри строки по вертикали.
        ) {
            // Сюда ты добавишь контент второй строки.
        }

        // Разделитель (Divider) под второй строкой.
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(), // Ширина строго 33% для соответствия первой строке.
            thickness = thickness, //берется из параметра
            color = color          //берется из параметра
        )
    }
}
