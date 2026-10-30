package com.example.mygos.ui.componentes

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygos.Usuario
import com.example.mygos.ui.theme.BotonOscuro
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.IconoGris
import com.example.mygos.ui.theme.InstrumentSerif
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import kotlin.math.roundToInt

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
        if (usuario.foto != null) {
            Image(
                painter = painterResource(usuario.foto),
                contentDescription = "Foto de ${usuario.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            return@Box
        }
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
    esClave: Boolean = false,
    lineas: Int = 1
) {
    var claveVisible by remember { mutableStateOf(false) }
    val hayError = error.isNotEmpty()
    val textoAbajo = if (hayError) error else ayuda

    // Cuando aparece o cambia el error el campo se mueve a los lados, como un "no" con la cabeza
    val desplazamiento = remember { Animatable(0f) }
    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            for (x in listOf(12f, -10f, 6f, -4f, 0f)) {
                desplazamiento.animateTo(x, tween(durationMillis = 50))
            }
        }
    }

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
        singleLine = lineas == 1,
        minLines = lineas,
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
        modifier = modifier
            .fillMaxWidth()
            .offset { IntOffset(desplazamiento.value.roundToInt(), 0) }
    )
}

// Barra de arriba de las pantallas internas: volver, titulo con subtitulo y una accion opcional a la derecha
@Composable
fun Cabecera(
    titulo: String,
    subtitulo: String,
    onVolver: () -> Unit,
    accion: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BotonCircular(icono = Icons.AutoMirrored.Filled.ArrowBack, descripcion = "Volver", onClick = onVolver)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitulo, color = TextoGris, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        if (accion != null) {
            Spacer(modifier = Modifier.width(8.dp))
            accion()
        }
    }
}

// Aviso para cuando se nego un permiso: explica para que se usa y deja abrir los ajustes de la app
@Composable
fun AvisoPermiso(texto: String, onReintentar: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Tarjeta)
            .border(1.dp, Naranja.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Text(texto, color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BotonContorno(texto = "Intentar de nuevo", onClick = onReintentar, modifier = Modifier.weight(1f))
            BotonContorno(
                texto = "Ajustes",
                onClick = {
                    // Si el usuario marco "no volver a preguntar", Android ya no muestra el dialogo: solo queda ajustes
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Parece un campo de texto pero se toca para abrir un selector (fecha u hora). Muestra el error igual que CampoTexto
@Composable
fun CampoSelector(
    valor: String,
    etiqueta: String,
    icono: ImageVector,
    error: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hayError = error.isNotEmpty()
    val forma = RoundedCornerShape(14.dp)
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(forma)
                .background(Tarjeta)
                .border(1.dp, if (hayError) Naranja else Color.White.copy(alpha = 0.08f), forma)
                .clickable { onClick() }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, tint = if (hayError) Naranja else IconoGris, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = valor.ifEmpty { etiqueta },
                color = if (valor.isEmpty()) TextoGris else TextoClaro,
                fontFamily = Nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
            if (hayError) {
                Icon(Icons.Filled.ErrorOutline, contentDescription = null, tint = Naranja, modifier = Modifier.size(20.dp))
            }
        }
        if (hayError) {
            Text(
                error,
                color = Naranja,
                fontFamily = Nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun Titulo(normal: String, cursiva: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 32.sp)) {
                append("$normal ")
            }
            withStyle(SpanStyle(fontFamily = InstrumentSerif, fontSize = 40.sp)) {
                append(cursiva)
            }
        },
        color = TextoClaro
    )
}

@Composable
fun CajaDato(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Tarjeta)
            .padding(14.dp)
    ) {
        Text(titulo, color = TextoGris, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, letterSpacing = 0.66.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(valor, color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 14.sp)
    }
}

@Composable
fun Subtitulo(texto: String) {
    Text(texto, color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 17.sp)
}

@Composable
fun Filtros(categorias: List<String>, seleccionada: String, onSeleccionar: (String) -> Unit, margen: Dp = 20.dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = margen),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (cat in categorias) {
            val activa = cat == seleccionada
            Box(
                modifier = Modifier
                    .height(38.dp)
                    .clip(CircleShape)
                    .background(if (activa) TextoClaro else Color.Transparent)
                    .border(1.dp, if (activa) TextoClaro else Color.White.copy(alpha = 0.16f), CircleShape)
                    .clickable { onSeleccionar(cat) }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cat,
                    color = if (activa) Fondo else TextoClaro,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

enum class Seccion { EVENTOS, TRIBUS, PERFIL }

// Barra de abajo que comparten Explorar y Mis tribus. El aviso naranja cuenta las tribus del usuario
@Composable
fun BarraInferior(actual: Seccion, misTribus: Int, onIr: (Seccion) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Fondo.copy(alpha = 0.92f))
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = Color.White.copy(alpha = 0.06f), thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 45.dp, end = 45.dp, top = 10.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ItemBarra(Icons.Filled.LocalActivity, "Eventos", activo = actual == Seccion.EVENTOS, onClick = { onIr(Seccion.EVENTOS) })
            ItemBarra(Icons.Outlined.Groups, "Tribus", activo = actual == Seccion.TRIBUS, aviso = misTribus, onClick = { onIr(Seccion.TRIBUS) })
            ItemBarra(Icons.Outlined.Person, "Perfil", activo = actual == Seccion.PERFIL, onClick = { onIr(Seccion.PERFIL) })
        }
    }
}

@Composable
fun ItemBarra(icono: ImageVector, texto: String, activo: Boolean, onClick: () -> Unit, aviso: Int = 0) {
    val color = if (activo) Naranja else IconoGris
    Column(
        modifier = Modifier
            .width(72.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Icon(
                imageVector = icono,
                contentDescription = texto,
                tint = color,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(26.dp)
            )
            if (aviso > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = 19.dp, y = (-4).dp)
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Naranja),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = aviso.toString(),
                        color = Color.White,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = texto,
            color = color,
            fontFamily = Nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp
        )
    }
}

@Composable
fun Etiqueta(texto: String) {
    Text(texto, color = TextoSuave, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
}
