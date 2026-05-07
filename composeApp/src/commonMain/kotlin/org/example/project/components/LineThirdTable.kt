package org.example.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.components.TableConfig
import org.example.project.models.DeviceInfoIni



@Composable
fun LineThirdTable(thickness: Dp = TableConfig.lineThickness, // Используем значение по умолчанию из конфига
                   color: Color = TableConfig.lineColor, selectedDevice: DeviceInfoIni? = null) {
    // Column служит контейнером, который выстраивает элементы вертикально.
    Column(
        modifier = Modifier.fillMaxWidth(), // Растягиваем контейнер на всю ширину экрана.
        horizontalAlignment = Alignment.End // ПРИЖИМАЕМ ВСЕ элементы (Row и Divider) вправо.
    ) {
        // Row - это сама строка с данными/кнопками третьей строки.
        Row(
            modifier = Modifier
                .fillMaxWidth() // Занимает ровно 33% ширины родителя (как и первая строка).
                .height(25.dp)       // Фиксированная высота для единообразия.
                .background(TableConfig.ThirdBackground), // Цвет фона.
            verticalAlignment = Alignment.CenterVertically // Выравнивание кнопок внутри строки по вертикали.
        ) {
            Text(
                text="ID:", modifier = Modifier
                    .padding(start = 8.dp),
                fontSize = 14.sp,
            fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            // 2. Длинное окно для вывода ID (ReadOnly)
            Box(
                modifier = Modifier
                    .weight(0.8f) // Занимает всё свободное место
                    .fillMaxHeight(0.8f) // Чуть ниже высоты строки для эстетики
                    .padding(end = 8.dp) // Отступ от правого края строки
                    .background(Color.LightGray, shape = MaterialTheme.shapes.extraSmall) //фон
                    .border(width = 1.dp, color = Color.LightGray, shape = MaterialTheme.shapes.extraSmall), // Рамка
                contentAlignment = Alignment.CenterStart // Текст прижат к левому краю окна
            ) {
                Text(
                    text = selectedDevice?.id ?: "---", // Сюда передавайте вашу переменную
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 6.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // Если текст слишком длинный — появится троеточие
                )
            }
        }

        // Разделитель (Divider) под второй строкой.
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(), // Ширина строго 33% для соответствия первой строке.
            thickness = thickness, //берется из параметра
            color = color          //берется из параметра
        )
    }
}