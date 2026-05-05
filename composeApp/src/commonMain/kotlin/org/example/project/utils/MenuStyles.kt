package org.example.project.utils

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Единый шаблон для всех пунктов меню
 */


@Composable
fun UniversalMenuItem(
    label: String,
    itemHeight: Dp,
    fontFamily: FontFamily = FontFamily.Monospace, // Добавили шрифт
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = label,
                fontSize = 12.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                softWrap = false,
                maxLines = 1
            )
        },
        modifier = Modifier
            .height(itemHeight)
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalSelector(
    label: String,
    selectedOption: String,
    options: List<String>,
    tooltipText: String,
    minWidth: Dp = 40.dp,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isEnabled = options.isNotEmpty()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = { PlainTooltip { Text(tooltipText, fontSize = 12.sp) } },
        state = rememberTooltipState()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 10.sp,
                modifier = Modifier.padding(end = 8.dp)
            )

            Box {
                Row(
                    modifier = Modifier
                        .border(1.dp, if (isEnabled) Color.Gray else Color.DarkGray)
                        .background(Color.White)
                        .height(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Основное поле с текстом
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .background(if (isEnabled) Color(0xFF0066CC) else Color.Gray)
                            .padding(horizontal = 4.dp)
                            .widthIn(min = minWidth),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selectedOption.uppercase(),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.Gray))

                    // Кнопка стрелочки
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(22.dp)
                            .background(if (isEnabled) Color(0xFFE0E0E0) else Color(0xFFB0B0B0))
                            .clickable(enabled = isEnabled) { expanded = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (isEnabled) Color.Black else Color.White
                        )
                    }
                }

                if (isEnabled) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.dp, Color.Black)
                            .widthIn(min = 120.dp)
                    ) {
                        options.forEach { option ->
                            UniversalMenuItem(
                                label = option,
                                itemHeight = 20.dp,
                                onClick = {
                                    onOptionSelected(option)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}