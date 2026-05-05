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