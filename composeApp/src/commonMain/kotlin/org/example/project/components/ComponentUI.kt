package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SimpleDropdown(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    width: Int
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(width.dp)
            .height(30.dp)
            .background(`TableColors.kt`.bgColor)
            .border(1.dp, `TableColors.kt`.dividerColor)
            .pointerInput(Unit) { detectTapGestures { expanded = true } }
    ) {
        Text(
            text = selectedOption,
            color = `TableColors.kt`.headerTextColor,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)
        )
        // Стрелочка
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(30.dp)
                .background(`TableColors.kt`.dividerColor),
            contentAlignment = Alignment.Center
        ) {
            Text("▼", fontSize = 10.sp, color = `TableColors.kt`.headerTextColor)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, fontSize = 12.sp) },
                    onClick = { onOptionSelected(option); expanded = false },
                    modifier = Modifier.height(28.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SplitMenuButton(
    text: String,
    onMainClick: () -> Unit,
    menuItems: List<String>,
    onMenuItemClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    // Используем dividerColor вместо хардкода 0xFF555555
    val buttonColor = ButtonDefaults.buttonColors(containerColor = `TableColors.kt`.dividerColor)

    Row(
        modifier = Modifier.height(30.dp).border(1.dp, Color.Gray, RectangleShape)
    ) {
        Button(
            onClick = onMainClick,
            modifier = Modifier.fillMaxHeight(),
            shape = RectangleShape,
            colors = buttonColor,
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text(text, fontSize = 11.sp, color = `TableColors.kt`.headerTextColor)
        }

        Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.Gray))

        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxHeight().width(30.dp),
            shape = RectangleShape,
            colors = buttonColor,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("▼", fontSize = 10.sp, color = `TableColors.kt`.headerTextColor)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            menuItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, fontSize = 12.sp) },
                    onClick = {
                        onMenuItemClick(item)
                        expanded = false
                    },
                    modifier = Modifier.height(28.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}