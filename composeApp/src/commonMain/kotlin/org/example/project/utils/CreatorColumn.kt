//CreatorColumn.kt
//Файл с функцией для создания столбцов с заголовком в любом месте кода

package org.example.project.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

/**
 * Универсальный компонент столбца таблицы.
 *
 * @param modifier Модификатор для настройки веса или ширины снаружи.
 * @param headerTitle Текст в верхней строке. Если null — строка не создается.
 * @param headerHeight Высота верхней строки.
 * @param headerBgColor Цвет фона верхней строки.
 * @param headerTextColor Цвет текста в верхней строке.
 * @param headerFontSize Размер шрифта в верхней строке.
 * @param isResizable Можно ли менять ширину столбца мышкой.
 * @param dividerThickness Толщина вертикальной линии разделителя.
 * @param dividerColor Стандартный цвет разделителя.
 * @param dividerActiveColor Цвет разделителя при перетаскивании.
 * @param onResize Ламбда-функция, вызываемая при движении мышки (принимает дельту изменения).
 * @param content Содержимое основной части столбца.
 */
@Composable
fun creatorColumn(
    modifier: Modifier = Modifier,
    headerTitle: String? = null,
    headerHeight: Dp = 30.dp,
    headerBgColor: Color = Color(0xFFF5F5F5),
    headerTextColor: Color = Color.Black,
    headerFontSize: Int = 12,
    isResizable: Boolean = true,
    dividerThickness: Dp = 2.dp,
    dividerColor: Color = Color.Gray,
    dividerActiveColor: Color = Color(0xFF0066CC),
    // Параметры для настройки цветов ручки
    handleColor: Color = Color.Gray.copy(alpha = 0.8f),
    handleActiveColor: Color = Color(0xFF0066CC),
    onResize: (Float) -> Unit = {},
    onHeaderClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }

    Row(modifier = modifier.fillMaxHeight().height(IntrinsicSize.Min)) {
        // 1. КОНТЕНТ СТОЛБЦА
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            headerTitle?.let { title ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeight)
                        .background(headerBgColor)
                        .clickable { onHeaderClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = headerTextColor,
                        fontSize = headerFontSize.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(dividerColor))
            }
            Column(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }

        // 2. РАЗДЕЛИТЕЛЬ И СИММЕТРИЧНАЯ РУЧКА
        Box(
            modifier = Modifier
                .width(dividerThickness)
                .fillMaxHeight()
                .zIndex(10f), // Выносим на самый верхний слой
            contentAlignment = Alignment.TopCenter // Центрируем содержимое по горизонтали
        ) {
            // Рисуем основную тонкую линию
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDragging) dividerActiveColor else dividerColor)
            )

            // РУЧКА (рисуется поверх линии)
            if (isResizable) {
                Box(
                    modifier = Modifier
                        // Позволяем ручке игнорировать ширину родителя в 2dp
                        .wrapContentWidth(unbounded = true)
                        .requiredWidth(12.dp) // Ширина всей интерактивной зоны
                        .height(headerHeight)
                        .background(
                            color = if (isDragging) handleActiveColor else handleColor,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                        )
                        // Вешаем жесты на саму широкую ручку
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { isDragging = true },
                                onDragEnd = { isDragging = false },
                                onDragCancel = { isDragging = false }
                            ) { change, dragAmount ->
                                change.consume()
                                onResize(dragAmount.x)
                            }
                        }
                )
            }
        }
    }
}