package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HeaderTable() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd // Прижимаем содержимое вправо
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.33f)
                .height(30.dp)
                .background(Color(0xFF2D2D2D))
                .padding(horizontal = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Здесь будут кнопки
        }
    }
}

@Preview
@Composable
fun HeaderPreview() {
    HeaderTable()
}