package com.example.mygos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.TextoClaro

// Circulo con las iniciales del usuario
@Composable
fun Avatar(
    usuario: Usuario,
    tamano: Dp,
    tamanoTexto: TextUnit,
    modifier: Modifier = Modifier,
    borde: Dp = 0.dp,
    colorBorde: Color = Color.Transparent,
    rayado: Boolean = false
) {
    var mod = modifier
        .size(tamano)
        .clip(CircleShape)
        .background(usuario.color)
    if (rayado) {
        mod = mod.rayas()
    }
    if (borde > 0.dp) {
        mod = mod.border(borde, colorBorde, CircleShape)
    }
    Box(modifier = mod, contentAlignment = Alignment.Center) {
        Text(
            text = usuario.iniciales,
            color = TextoClaro,
            fontFamily = Nunito,
            fontWeight = FontWeight.Black,
            fontSize = tamanoTexto
        )
    }
}

// Lineas diagonales blancas suaves que usa el diseño en las fotos de ejemplo
fun Modifier.rayas(): Modifier = this.drawBehind {
    val paso = 17.dp.toPx()
    var x = -size.height
    while (x < size.width) {
        drawLine(
            color = Color.White.copy(alpha = 0.07f),
            start = Offset(x, size.height),
            end = Offset(x + size.height, 0f),
            strokeWidth = 2.dp.toPx()
        )
        x += paso
    }
}
