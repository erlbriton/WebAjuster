package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ParameterRow(
    val pn: String, val name: String, val description: String,
    val unit: String, val baseHex: String, val basePhys: String,
    val devHex: String, val devPhys: String
)

@Composable
fun ParameterTable(parameters: List<ParameterRow>) {
    val bgColor = Color(0xFF1A1A1A)
    val headerBgColor = Color(0xFF2D2D2D)
    val groupHeaderBgColor = Color(0xFF3D3D3D)
    val rowBgColor = Color(0xFF222222)
    val dividerColor = Color(0xFF555555)
    val textColor = Color(0xFFE0E0E0)
    val headerTextColor = Color.White

    // Увеличено до 60 строк
    val displayParameters = remember(parameters) {
        parameters + List(60) { i ->
            ParameterRow("p${20000 + i}", "Param_$i", "Описание $i", "U", "0x00", "0.0", "0x00", "0.0")
        }
    }

    val listState = rememberLazyListState()
    val colWeights = remember { mutableStateListOf(1f, 1.5f, 2.5f, 1f, 1f, 1f, 1f, 1f) }

    val paramWeight by remember { derivedStateOf { colWeights.subList(0, 4).sum() } }
    val baseWeight by remember { derivedStateOf { colWeights.subList(4, 6).sum() } }
    val deviceWeight by remember { derivedStateOf { colWeights.subList(6, 8).sum() } }

    @Composable
    fun VerticalDivider() = Box(modifier = Modifier.fillMaxHeight().width(2.dp).background(dividerColor))

    Column(modifier = Modifier.fillMaxSize().background(bgColor)) {
        Column(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Row(modifier = Modifier.fillMaxWidth().height(30.dp).background(groupHeaderBgColor)) {
                Box(Modifier.weight(paramWeight).fillMaxHeight().pointerInput(Unit) {
                    detectDragGestures { change, drag ->
                        change.consume()
                        val factor = drag.x / 200f
                        colWeights[3] = (colWeights[3] + factor).coerceAtLeast(0.1f)
                        colWeights[4] = (colWeights[4] - factor).coerceAtLeast(0.1f)
                    }
                }, Alignment.Center) { Text("Параметр", color = headerTextColor, fontWeight = FontWeight.Bold) }
                VerticalDivider()
                Box(Modifier.weight(baseWeight).fillMaxHeight().pointerInput(Unit) {
                    detectDragGestures { change, drag ->
                        change.consume()
                        val factor = drag.x / 200f
                        colWeights[5] = (colWeights[5] + factor).coerceAtLeast(0.1f)
                        colWeights[6] = (colWeights[6] - factor).coerceAtLeast(0.1f)
                    }
                }, Alignment.Center) { Text("База", color = headerTextColor, fontWeight = FontWeight.Bold) }
                VerticalDivider()
                Box(Modifier.weight(deviceWeight).fillMaxHeight(), Alignment.Center) { Text("Контроллер", color = headerTextColor, fontWeight = FontWeight.Bold) }
            }
            Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(dividerColor))

            Row(modifier = Modifier.fillMaxWidth().height(25.dp).background(headerBgColor)) {
                val headers = listOf("PN", "Имя", "Описание", "Ед. изм.", "hex", "Физич.", "hex", "Физич.")
                headers.forEachIndexed { i, text ->
                    Box(
                        Modifier.weight(colWeights[i]).fillMaxHeight().pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                val delta = dragAmount.x / 200f
                                if (i < colWeights.size - 1) {
                                    colWeights[i] = (colWeights[i] + delta).coerceAtLeast(0.05f)
                                    colWeights[i + 1] = (colWeights[i + 1] - delta).coerceAtLeast(0.05f)
                                }
                            }
                        },
                        Alignment.Center
                    ) {
                        Text(text, color = headerTextColor, fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                    if (i < 7) VerticalDivider()
                }
            }
        }
        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(dividerColor))

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                items(displayParameters) { p ->
                    Row(modifier = Modifier.fillMaxWidth().height(25.dp).background(rowBgColor), verticalAlignment = Alignment.CenterVertically) {
                        val cells = listOf(p.pn, p.name, p.description, p.unit, p.baseHex, p.basePhys, p.devHex, p.devPhys)
                        cells.forEachIndexed { i, text ->
                            Box(Modifier.weight(colWeights[i]).fillMaxHeight(), Alignment.Center) {
                                Text(text, color = textColor, fontSize = 12.sp, textAlign = TextAlign.Center)
                            }
                            if (i < 7) VerticalDivider()
                        }
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = listState)
            )
        }
    }
}