package com.example.mygos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// La app siempre es oscura, como en el diseño
private val ColoresMygos = darkColorScheme(
    primary = Naranja,
    secondary = Amarillo,
    background = Fondo,
    surface = Fondo,
    onBackground = TextoClaro,
    onSurface = TextoClaro
)

@Composable
fun MygosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresMygos,
        typography = Typography,
        content = content
    )
}
