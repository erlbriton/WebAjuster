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
fun HeaderTable(
    thickness: Dp = TableConfig.lineThickness, // Используем значение по умолчанию из конфига
    color: Color = TableConfig.lineColor       // Используем значение по умолчанию из конфига
) {
    // Верхняя граница
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = thickness, //берется из параметра
        color = color          //берется из параметра
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Color(0xFFC4BEBE))
                .padding(horizontal = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопки
        }

        // Нижняя граница заголовка
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = thickness, // ЗАМЕНЕНО: теперь берется из параметра
            color = color          // ЗАМЕНЕНО: теперь берется из параметра
        )
    }
}
