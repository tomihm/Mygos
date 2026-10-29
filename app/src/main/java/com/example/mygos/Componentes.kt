package com.example.mygos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygos.ui.theme.BotonOscuro
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris

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

@Composable
fun BotonCircular(
    icono: ImageVector,
    descripcion: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fondo: Color = BotonOscuro
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(fondo)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun BotonNaranja(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null
) {
    val forma = RoundedCornerShape(18.dp)
    Button(
        onClick = onClick,
        shape = forma,
        colors = ButtonDefaults.buttonColors(containerColor = Naranja),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(14.dp, forma, spotColor = Naranja, ambientColor = Naranja)
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        Text(
            text = texto,
            color = Color.White,
            fontFamily = Nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun CampoTexto(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    tipo: KeyboardType = KeyboardType.Text,
    esClave: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta, fontFamily = Nunito) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        textStyle = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 15.sp),
        keyboardOptions = KeyboardOptions(keyboardType = tipo),
        visualTransformation = if (esClave) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextoClaro,
            unfocusedTextColor = TextoClaro,
            focusedContainerColor = Tarjeta,
            unfocusedContainerColor = Tarjeta,
            focusedBorderColor = Naranja,
            unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
            focusedLabelColor = Naranja,
            unfocusedLabelColor = TextoGris,
            cursorColor = Naranja
        ),
        modifier = modifier.fillMaxWidth()
    )
}
