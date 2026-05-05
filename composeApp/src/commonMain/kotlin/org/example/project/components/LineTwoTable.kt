package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.utils.ManualAndAutoInputField
import org.example.project.utils.UniversalSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineTwoTable(
    thickness: Dp = TableConfig.lineThickness,
    color: Color = TableConfig.lineColor
) {
    // Состояния из вашего исходного файла
    var selectedProtocol by remember { mutableStateOf("Modbus RTU") }
    val protocolOptions = listOf("Modbus RTU", "Modbus TCP")

    var selectedCom by remember { mutableStateOf("") }
    var comOptions by remember { mutableStateOf(listOf<String>()) }

    var chosenSpeed by remember { mutableStateOf("115200") }
    val speedOptions =
        listOf("921600", "460800", "230400", "115200", "57600", "38400", "19200", "9600")

    var frameEndInput by remember { mutableStateOf("20") }

    var addressInput by remember { mutableStateOf("x01") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start // ГАРАНТИРУЕТ начало слева
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(TableConfig.TwoBackground),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Контейнер для всех элементов
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 2.dp) // Минимальный зазор от левого края
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start // Внутреннее выравнивание текстов
            ) {
                Text(
                    text = "Настройки связи",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 1. Протокол
                    UniversalSelector(
                        label = "BUS",
                        selectedOption = selectedProtocol,
                        options = protocolOptions,
                        tooltipText = "Протокол передачи данных",
                        onOptionSelected = { selectedProtocol = it }
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // 2. COM-порт
                    UniversalSelector(
                        label = "COM",
                        selectedOption = selectedCom,
                        options = comOptions,
                        tooltipText = if (comOptions.isEmpty()) "Порты не найдены" else "Выберите доступный COM-порт",
                        minWidth = 40.dp,
                        onOptionSelected = { selectedCom = it }
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // 3. Скорость
                    UniversalSelector(
                        label = "BPS",
                        selectedOption = chosenSpeed,
                        options = speedOptions,
                        tooltipText = "Скорость обмена (бит/с)",
                        minWidth = 55.dp,
                        onOptionSelected = { chosenSpeed = it }
                    )

                    ManualAndAutoInputField(
                        label = "FrameEnd",              // Метка перед окном
                        value = frameEndInput,       // Текущее значение
                        tooltipText = "Время ожидания окончания пакета[mc]", // Подсказка
                        windowColor = Color.White, // Цвет окна для ручного ввода
                        width = 30.dp,             // Нужная ширина окна
                        onValueChange = { newValue ->
                            // Это единственный путь изменения значения — через клавиатуру
                            frameEndInput = newValue
                        }
                    )
                    ManualAndAutoInputField(
                        label = " Адрес",              // Метка перед окном
                        value = addressInput,       // Текущее значение
                        tooltipText = "Адрес устройства в сети", // Подсказка
                        windowColor = Color.White, // Цвет окна для ручного ввода
                        width = 30.dp,             // Нужная ширина окна
                        onValueChange = { newValue ->
                            // Это единственный путь изменения значения — через клавиатуру
                            addressInput = newValue
                        }
                    )
                }
            }
            // Пружина выталкивает всё влево
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}