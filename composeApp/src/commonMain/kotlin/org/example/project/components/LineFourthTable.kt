package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.utils.ManualAndAutoInputField
import org.example.project.utils.TableIconButton

fun getJsDateString(): String = js("""
    (function() {
        var d = new Date();
        var day = ('0' + d.getDate()).slice(-2);
        var month = ('0' + (d.getMonth() + 1)).slice(-2);
        var year = d.getFullYear();
        return day + '.' + month + '.' + year;
    })()
""")
@Composable
fun LineForthTable(thickness: Dp = TableConfig.lineThickness, // Используем значение по умолчанию из конфига
                   color: Color = TableConfig.lineColor) {
    // Column служит контейнером, который выстраивает элементы вертикально.
    var typeMechanism by remember { mutableStateOf("") }
    var dateSet by remember { mutableStateOf("29.01.1964") }

    Column(
        modifier = Modifier.fillMaxWidth(), // Растягиваем контейнер на всю ширину экрана.
        horizontalAlignment = Alignment.End // ПРИЖИМАЕМ ВСЕ элементы (Row и Divider) вправо.
    ) {
        // Row - это сама строка с данными/кнопками третьей строки.
        Row(
            modifier = Modifier
                .fillMaxWidth() // Занимает ровно 33% ширины родителя (как и первая строка).
                .height(25.dp)       // Фиксированная высота для единообразия.
                .background(TableConfig.FourthBackground), // Цвет фона.
            verticalAlignment = Alignment.CenterVertically // Выравнивание кнопок внутри строки по вертикали.
        ) {
            //Окно "Тип механизма"
            ManualAndAutoInputField(
                label = "Тип механизма",              // Метка перед окном
                value = typeMechanism,       // Текущее значение
                tooltipText = "Тип механизма", // Подсказка
                windowColor = Color.White, // Цвет окна для ручного ввода
                width = 100.dp,             // Нужная ширина окна
                onValueChange = { newValue ->
                    // Это единственный путь изменения значения — через клавиатуру
                    typeMechanism = newValue
                }
            )
            //Окно "Дата"
            ManualAndAutoInputField(
                label = "Дата",              // Метка перед окном
                value = dateSet,       // Текущее значение
                tooltipText = "Дата", // Подсказка
                windowColor = Color.White, // Цвет окна для ручного ввода
                width = 80.dp,             // Нужная ширина окна
                onValueChange = { newValue ->
                    // Это единственный путь изменения значения — через клавиатуру
                    dateSet = newValue
                }
            )
            TableIconButton(
                text = "Сегодня",
                tooltipText = "Использовать текущие дату и время",
                backgroundColor = Color(0xFFBBAFAF),
                onClick = {
// Получаем текущий момент времени
                    dateSet = getJsDateString()
                }
            )

        }

        // Разделитель (Divider) под второй строкой.
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(), // Ширина строго 33% для соответствия первой строке.
            thickness = thickness, //берется из параметра
            color = color          //берется из параметра
        )
    }
}