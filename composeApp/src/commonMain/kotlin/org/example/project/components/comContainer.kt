package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun ComContainer() {
    var tableWidth by remember { mutableStateOf(800.dp) }//Начальный размер всей таблицы
    var leftColumnWeight by remember { mutableStateOf(0.25f) }//Начальный размер левого столбца
    val scrollState = rememberScrollState()
    val lineThickness = 2.dp // Толщина всех линий
    val lineColor = Color.Gray // Цвет всех линий

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxAllowedWidth = maxWidth

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            Row(modifier = Modifier.wrapContentWidth().fillMaxHeight()) {

                // --- Ручка изменения размера ---
                Box(
                    modifier = Modifier
                        .width(6.dp)
                       // .fillMaxHeight(0.3f)
                        .height(34.dp)
                        .background(Color.Red.copy(alpha = 0.5f))
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                tableWidth -= dragAmount.x.toDp()
                                tableWidth = tableWidth.coerceIn(150.dp, maxAllowedWidth)
                            }
                        }
                )
                // --- Сама Таблица ---
                // Убираем verticalScroll отсюда, если хотим, чтобы нижняя часть растягивалась
                // Либо оставляем его, но тогда высота будет зависеть от контента.
                Column(
                    modifier = Modifier
                        .width(tableWidth)
                        .fillMaxHeight() // Тянем на всю высоту
                        .border(width = TableConfig.lineThickness, color = TableConfig.lineColor)
                ) {
                    HeaderTable()
                    // LineTwoTable()
                    // --- Секция двух столбцов ---
                    // weight(1f) заставит этот блок занять все оставшееся место по вертикали
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        // Левый столбец
                        Box(
                            modifier = Modifier
                                .weight(leftColumnWeight)
                                .fillMaxHeight()
                        )
                        // Разделитель
                        Box(
                            modifier = Modifier
                                .width(TableConfig.lineThickness)
                                .fillMaxHeight()
                                .background(TableConfig.lineColor)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        val delta = dragAmount.x / tableWidth.value
                                        leftColumnWeight = (leftColumnWeight + delta).coerceIn(0.1f, 0.9f)
                                    }
                                }
                        )
                        // Правый столбец
                        Box(
                            modifier = Modifier
                                .weight(1f - leftColumnWeight)
                                .fillMaxHeight()
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                LineTwoTable()//Вторая строка
                                LineThirdTable()//Третья строка
                                LineForthTable()//Четвертая строка
                                LineFifthTable()//Пятая строка
                            }
                        }
                    }
                }
            }
        }
    }
}