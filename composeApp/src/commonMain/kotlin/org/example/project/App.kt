package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }

        // --- Новое состояние для UART ---
        var uartStatus by remember { mutableStateOf("Устройство не выбрано") }
        val scope = rememberCoroutineScope()
        // --------------------------------

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Кнопка для UART (CP2103)
            Button(onClick = {
                scope.launch {
                    uartStatus = "Ожидание выбора порта..."
                    uartStatus = requestUartDevice()
                }
            }) {
                Text("Найти CP2103")
            }

            Text(text = uartStatus, style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(16.dp)) // Отступ между кнопками

            // Ваша стандартная кнопка
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }



            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}
expect fun findSerialPort()