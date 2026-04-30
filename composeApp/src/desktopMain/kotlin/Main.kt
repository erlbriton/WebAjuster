import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.window.WindowPlacement
import org.example.project.App

fun main() = application {
    // Создаем состояние окна с параметром полноэкранного размещения
    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState, // Передаем состояние окну
        title = "Adjuster - Desktop Debug"
    ) {
        App()
    }
}