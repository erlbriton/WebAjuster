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
fun LineTwoTable(
    thickness: Dp = TableConfig.lineThickness,
    color: Color = TableConfig.lineColor
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        // Контейнер строки
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(TableConfig.TwoBackground),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Используем Box, чтобы позиционировать текст независимо от других элементов
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp), // Отступ текста от краев
                contentAlignment = Alignment.TopStart // Прижимаем текст к левому краю и центру по вертикали
            ) {
                // Неизменяемый текст
                Text(
                    text = "Настройки связи",
                    color = Color.White, // Цвет текста (белый хорошо читается на синем фоне)
                    style = MaterialTheme.typography.bodySmall
                )

                // Если здесь будут кнопки, их можно обернуть в другой Row с выравниванием по правому краю
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Место для ваших кнопок
                }
            }
        }

        // Разделитель (Divider) под второй строкой
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = thickness,
            color = color
        )
    }
}