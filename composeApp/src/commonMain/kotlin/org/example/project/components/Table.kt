package org.example.project.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import org.example.project.models.PortData // Предполагаемая модель данных

@Composable
fun CustomTable(dataList: List<PortData>) {
    LazyColumn {
        item {
           // HeaderTable() // Используем компонент из соседнего файла
        }
        items(dataList) { item ->
            // Здесь будет вызов строки таблицы (например, TableRow(item))
        }
    }
}