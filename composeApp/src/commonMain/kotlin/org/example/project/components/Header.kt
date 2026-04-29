package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HeaderTable() {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(), // Линия тоже 33%
        thickness = 3.dp,
        color = Color.Black
    )
    // Column расставляет элементы вертикально (сверху вниз)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End // Прижимаем всё к правому краю
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Занимает 33% ширины
                .height(30.dp)
                .background(Color(0xFF2D2D2D))
                .padding(horizontal = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопки
        }

        // Линия теперь пойдет СРАЗУ ПОД строкой, так как это Column
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(), // Линия тоже 33%
            thickness = 3.dp,
            color = Color.Black
        )
    }
}
