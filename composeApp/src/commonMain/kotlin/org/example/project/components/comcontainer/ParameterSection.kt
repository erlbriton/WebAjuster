package org.example.project.components.comcontainer

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.models.DeviceInfoIni

@Composable
fun ParameterSection(
    selectedDevice: DeviceInfoIni?,
    tableScrollState: ScrollState,
    weightCol2: Float,
    weightCol3: Float,
    onNameResize: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // 1 и 4 столбцы строго одинаковой ширины
    val sideColumnWidth = 55.dp

    // Состояние для подсветки центральной линии
    var isDraggingName by remember { mutableStateOf(false) }

    val activeColor = Color(0xFF2196F3)
    val inactiveColor = Color.Black

    Column(modifier = modifier.fillMaxSize()) {
        // 1. ШАПКА
        Row(modifier = Modifier.fillMaxWidth().height(28.dp).background(Color(0xFFE0E0E0))) {
            HeaderSubCell(" №", Modifier.width(sideColumnWidth))

            // Здесь ручка ЕСТЬ
            HeaderSubCell(
                text = "Имя",
                modifier = Modifier.weight(weightCol2),
                isResizable = true,
                isDragging = isDraggingName,
                onDraggingStateChange = { isDraggingName = it },
                onResize = onNameResize
            )

            // Здесь ручку УБРАЛИ (isResizable = false)
            HeaderSubCell(
                text = "Описание",
                modifier = Modifier.weight(weightCol3),
                isResizable = false
            )

            HeaderSubCell("Ед. изм.", Modifier.width(sideColumnWidth))
        }

        // 2. ОБЛАСТЬ ДАННЫХ
        Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
            // PN
            Column(Modifier.width(sideColumnWidth).fillMaxHeight().drawRightBorder(inactiveColor).verticalScroll(tableScrollState)) {
                selectedDevice?.flashParameters?.forEachIndexed { i, _ ->
                    ParameterCell((i + 1).toString(), Alignment.Center)
                }
            }
            // Name (Линия подсвечивается при ресайзе)
            Column(Modifier.weight(weightCol2).fillMaxHeight().drawRightBorder(if (isDraggingName) activeColor else inactiveColor, if (isDraggingName) 2f else 1f).verticalScroll(tableScrollState)) {
                selectedDevice?.flashParameters?.forEach { p ->
                    ParameterCell(p.idName, Alignment.CenterStart)
                }
            }
            // Description (Статичная линия)
            Column(Modifier.weight(weightCol3).fillMaxHeight().drawRightBorder(inactiveColor).verticalScroll(tableScrollState)) {
                selectedDevice?.flashParameters?.forEach { p ->
                    ParameterCell(p.description, Alignment.CenterStart)
                }
            }
            // Meas.unit (Без линии справа)
            Column(Modifier.width(sideColumnWidth).fillMaxHeight().verticalScroll(tableScrollState)) {
                selectedDevice?.flashParameters?.forEach { p ->
                    ParameterCell(p.unit, Alignment.Center)
                }
            }
        }
    }
}

@Composable
fun HeaderSubCell(
    text: String,
    modifier: Modifier,
    drawRightBorder: Boolean = true,
    isResizable: Boolean = false,
    isDragging: Boolean = false,
    onDraggingStateChange: (Boolean) -> Unit = {},
    onResize: (Float) -> Unit = {}
) {
    // Явно задаем цвет здесь (тот же, что и в данных)
    val mainLineColor = Color(0xFF333333)
    val activeColor = Color(0xFF2196F3)

    val currentLineColor = if (isDragging) activeColor else mainLineColor
    val currentLineWidth = if (isDragging) 2f else 1f

    Box(
        modifier = modifier
            .fillMaxHeight()
            .then(
                if (drawRightBorder) Modifier.drawRightBorder(currentLineColor, currentLineWidth)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        if (isResizable) {
            Box(
                Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(14.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { onDraggingStateChange(true) },
                            onDragEnd = { onDraggingStateChange(false) },
                            onDragCancel = { onDraggingStateChange(false) },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                onResize(dragAmount.x)
                            }
                        )
                    },
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 1.dp)
                        .width(4.dp)
                        .height(16.dp)
                        .background(
                            color = if (isDragging) activeColor else Color(0xFF666666),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun ParameterCell(text: String, alignment: Alignment) {
    Box(
        modifier = Modifier.fillMaxWidth().height(24.dp).padding(horizontal = 4.dp).drawBottomBorder(),
        contentAlignment = alignment
    ) {
        Text(text, fontSize = 11.sp, maxLines = 1, softWrap = false, color = Color.Black)
    }
}

// РАСШИРЕНИЯ
fun Modifier.drawRightBorder(color: Color, width: Float = 1f) = this.drawBehind {
    drawLine(
        color = color,
        start = Offset(size.width, 0f),
        end = Offset(size.width, size.height),
        strokeWidth = width
    )
}

fun Modifier.drawBottomBorder() = this.drawBehind {
    drawLine(
        color = Color(0xFFF0F0F0),
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        strokeWidth = 1f
    )
}