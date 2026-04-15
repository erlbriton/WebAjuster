package org.example.project

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // Берем body страницы. Он 100% поддерживает attachShadow.
    val body = document.body ?: return

    ComposeViewport(body) {
        App()
    }
}