package com.example.mygos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.mygos.ui.theme.IconoGris
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

// Boton secundario: solo borde, sin relleno
@Composable
fun BotonContorno(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    colorTexto: Color = TextoClaro,
    colorBorde: Color = Color.White.copy(alpha = 0.15f),
    fondo: Color = Color.Transparent
) {
    val forma = RoundedCornerShape(18.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(forma)
            .background(fondo)
            .border(1.dp, colorBorde, forma)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icon(imageVector = icono, contentDescription = null, tint = colorTexto, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = texto, color = colorTexto, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
    }
}

// Campo del formulario. Si llega un error, el borde y el mensaje se pintan de naranja y aparece el icono de alerta.
// La ayuda es un texto gris que se muestra mientras no hay error
@Composable
fun CampoTexto(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    error: String = "",
    ayuda: String = "",
    tipo: KeyboardType = KeyboardType.Text,
    esClave: Boolean = false
) {
    var claveVisible by remember { mutableStateOf(false) }
    val hayError = error.isNotEmpty()
    val textoAbajo = if (hayError) error else ayuda

    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta, fontFamily = Nunito) },
        leadingIcon = { Icon(imageVector = icono, contentDescription = null) },
        trailingIcon = when {
            esClave -> {
                {
                    IconButton(onClick = { claveVisible = !claveVisible }) {
                        Icon(
                            imageVector = if (claveVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (claveVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                }
            }
            hayError -> {
                { Icon(imageVector = Icons.Filled.ErrorOutline, contentDescription = null) }
            }
            else -> null
        },
        isError = hayError,
        supportingText = if (textoAbajo.isNotEmpty()) {
            { Text(textoAbajo, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
        } else null,
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        textStyle = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 15.sp),
        keyboardOptions = KeyboardOptions(keyboardType = tipo),
        visualTransformation = if (esClave && !claveVisible) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextoClaro,
            unfocusedTextColor = TextoClaro,
            errorTextColor = TextoClaro,
            focusedContainerColor = Tarjeta,
            unfocusedContainerColor = Tarjeta,
            errorContainerColor = Tarjeta,
            focusedBorderColor = Naranja,
            unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
            errorBorderColor = Naranja,
            focusedLabelColor = Naranja,
            unfocusedLabelColor = TextoGris,
            errorLabelColor = Naranja,
            focusedLeadingIconColor = Naranja,
            unfocusedLeadingIconColor = IconoGris,
            errorLeadingIconColor = Naranja,
            focusedTrailingIconColor = IconoGris,
            unfocusedTrailingIconColor = IconoGris,
            errorTrailingIconColor = Naranja,
            focusedSupportingTextColor = TextoGris,
            unfocusedSupportingTextColor = TextoGris,
            errorSupportingTextColor = Naranja,
            cursorColor = Naranja,
            errorCursorColor = Naranja
        ),
        modifier = modifier.fillMaxWidth()
    )
}
