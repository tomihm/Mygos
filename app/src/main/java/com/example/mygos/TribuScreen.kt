package com.example.mygos

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.data.eventosIniciales
import com.example.mygos.data.tribusIniciales
import com.example.mygos.ui.theme.Amarillo
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.InstrumentSerif
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoDescripcion
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.ui.theme.Verde
import com.example.mygos.viewmodel.TribuDetalleEstado
import com.example.mygos.viewmodel.TribuDetalleViewModel
import kotlinx.coroutines.delay

@Composable
fun TribuDetalleRuta(
    tribuId: Int,
    usuario: Usuario,
    onVolver: () -> Unit,
    onUnido: (Tribu) -> Unit,
    viewModel: TribuDetalleViewModel = viewModel { TribuDetalleViewModel(tribuId, usuario) }
) {
    val context = LocalContext.current
    val estado by viewModel.estado.collectAsState()

    LaunchedEffect(estado.unido) {
        val tribu = estado.tribu
        if (estado.unido && tribu != null) {
            onUnido(tribu)
            viewModel.unionMostrada()
        }
    }
    LaunchedEffect(estado.error) {
        if (estado.error.isNotEmpty()) {
            Toast.makeText(context, estado.error, Toast.LENGTH_SHORT).show()
            viewModel.errorMostrado()
        }
    }

    if (estado.tribu != null && estado.evento != null) {
        TribuDetalleScreen(
            estado = estado,
            onVolver = onVolver,
            onUnirme = viewModel::unirse,
            onPedirSalir = viewModel::pedirSalir,
            onCancelarSalir = viewModel::cancelarSalir,
            onSalir = viewModel::salir
        )
    }
}

@Composable
fun TribuDetalleScreen(
    estado: TribuDetalleEstado,
    onVolver: () -> Unit,
    onUnirme: () -> Unit,
    onPedirSalir: () -> Unit,
    onCancelarSalir: () -> Unit,
    onSalir: () -> Unit
) {
    val tribu = estado.tribu ?: return
    val evento = estado.evento ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Box {
                // los nombres ya salen en "Quienes son"; aqui taparian el titulo
                GrillaIntegrantes(tribu.integrantes, conNombres = false)
                // degradado para que el titulo se lea sobre los colores
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Brush.verticalGradient(0.35f to Color.Transparent, 1f to Fondo))
                )
                BotonCircular(
                    icono = Icons.AutoMirrored.Filled.ArrowBack,
                    descripcion = "Volver",
                    onClick = onVolver,
                    fondo = Fondo.copy(alpha = 0.6f),
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(start = 16.dp, top = 10.dp)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 20.dp)
                ) {
                    ChipAfinidad("${tribu.afinidad}% afinidad contigo")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = tribu.nombre,
                        color = TextoClaro,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Black,
                        fontSize = 30.sp,
                        lineHeight = 32.sp
                    )
                    Text(text = evento.titulo, color = TextoClaro, fontFamily = Nunito, fontSize = 14.sp)
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CajaDato("ENCUENTRO", tribu.plan.replaceFirstChar { it.uppercase() }, Modifier.weight(1f))
                    CajaDato("TRIBU", "${tribu.integrantes.size} de ${tribu.cupos} personas", Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(22.dp))
                Subtitulo("Intereses")
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (interes in tribu.intereses) {
                        ChipInteres(interes)
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))
                Subtitulo("Quiénes son")
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    for (persona in tribu.integrantes) {
                        Integrante(persona)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Los botones cambian segun si el usuario ya esta en la tribu o si quedan cupos
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 12.dp)) {
            when {
                estado.soyIntegrante -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Verde, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ya eres parte de esta tribu", color = Verde, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    BotonContorno(texto = "Salir de la tribu", onClick = onPedirSalir, icono = Icons.AutoMirrored.Filled.Logout)
                }
                estado.llena -> {
                    BotonContorno(texto = "Esta tribu ya está llena", onClick = {}, colorTexto = TextoGris)
                }
                else -> {
                    BotonNaranja(texto = "Unirme a la tribu", onClick = onUnirme, icono = Icons.Filled.GroupAdd)
                }
            }
        }
    }

    if (estado.confirmarSalida) {
        AlertDialog(
            onDismissRequest = onCancelarSalir,
            title = { Text("¿Salir de ${tribu.nombre}?", fontFamily = Nunito, fontWeight = FontWeight.Black) },
            text = { Text("Tu cupo quedará libre para otra persona.", fontFamily = Nunito) },
            confirmButton = {
                TextButton(onClick = onSalir) {
                    Text("Salir", color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = onCancelarSalir) {
                    Text("Cancelar", color = TextoSuave, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
                }
            },
            containerColor = Tarjeta,
            titleContentColor = TextoClaro,
            textContentColor = TextoDescripcion
        )
    }
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
fun Integrante(persona: Usuario) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(60.dp)) {
        Box {
            Avatar(usuario = persona, tamano = 52.dp, tamanoTexto = 16.sp)
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Verificado",
                tint = Verde,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Fondo)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(persona.nombre, color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
    }
}

@Composable
fun YaTienesTribuRuta(
    tribuId: Int,
    usuario: Usuario,
    onVerMisTribus: () -> Unit,
    onVerOtrasTribus: () -> Unit,
    viewModel: TribuDetalleViewModel = viewModel { TribuDetalleViewModel(tribuId, usuario) }
) {
    val estado by viewModel.estado.collectAsState()
    val tribu = estado.tribu
    val evento = estado.evento
    if (tribu != null && evento != null) {
        YaTienesTribuScreen(tribu, evento, onVerMisTribus, onVerOtrasTribus)
    }
}

// Donde va cada burbuja (x, y, tamano en dp). Si la tribu tiene mas de 5 integrantes no se dibujan todos
private val burbujas = listOf(
    Triple(40, 90, 112),
    Triple(200, 70, 96),
    Triple(130, 200, 104),
    Triple(30, 240, 72),
    Triple(250, 190, 86)
)

@Composable
fun YaTienesTribuScreen(
    tribu: Tribu,
    evento: Evento,
    onVerMisTribus: () -> Unit,
    onVerOtrasTribus: () -> Unit
) {
    // La tarjeta sube y aparece despues de que entran las burbujas
    val tarjeta = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(450)
        tarjeta.animateTo(1f, tween(durationMillis = 400))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Naranja)
    ) {
        Box(
            modifier = Modifier
                .offset(x = (-70).dp, y = 70.dp)
                .size(290.dp)
                .border(30.dp, Amarillo, CircleShape)
        )
        Box(
            modifier = Modifier
                .offset(x = 150.dp, y = 150.dp)
                .size(320.dp)
                .border(30.dp, TextoClaro, CircleShape)
        )
        tribu.integrantes.take(burbujas.size).forEachIndexed { i, persona ->
            val (x, y, tamano) = burbujas[i]
            BurbujaIntegrante(persona, x, y, tamano, retraso = i * 110L)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp)
                .graphicsLayer {
                    alpha = tarjeta.value
                    translationY = (1f - tarjeta.value) * 120f
                }
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White.copy(alpha = 0.14f))
                .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(30.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 30.sp)) {
                        append("¡Ya tienes ")
                    }
                    withStyle(SpanStyle(fontFamily = InstrumentSerif, fontSize = 40.sp)) {
                        append("tribu!")
                    }
                },
                color = TextoClaro
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${tribu.nombre} · ${tribu.integrantes.size} personas para ir juntas a ${evento.titulo}.",
                color = TextoClaro,
                fontFamily = Nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = TextoClaro, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Todos con identidad verificada", color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(18.dp))
            BotonContorno(
                texto = "Ver mis tribus",
                onClick = onVerMisTribus,
                colorTexto = Fondo,
                colorBorde = TextoClaro,
                fondo = TextoClaro
            )
            Spacer(modifier = Modifier.height(10.dp))
            BotonContorno(
                texto = "Ver otras tribus",
                onClick = onVerOtrasTribus,
                colorBorde = Color.White.copy(alpha = 0.3f),
                fondo = Color.White.copy(alpha = 0.12f)
            )
        }
    }
}

// Cada burbuja crece desde cero con un rebote; el retraso hace que entren una tras otra
@Composable
fun BurbujaIntegrante(persona: Usuario, x: Int, y: Int, tamano: Int, retraso: Long) {
    val escala = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(retraso)
        escala.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
    }
    Avatar(
        usuario = persona,
        tamano = tamano.dp,
        tamanoTexto = (tamano / 3.2).sp,
        borde = 4.dp,
        colorBorde = TextoClaro,
        modifier = Modifier
            .offset(x = x.dp, y = y.dp)
            .graphicsLayer {
                scaleX = escala.value
                scaleY = escala.value
            }
            .shadow(12.dp, CircleShape)
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun TribuDetallePreview() {
    MygosTheme {
        TribuDetalleScreen(
            estado = TribuDetalleEstado(tribu = tribusIniciales[0], evento = eventosIniciales[0]),
            onVolver = {},
            onUnirme = {},
            onPedirSalir = {},
            onCancelarSalir = {},
            onSalir = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun YaTienesTribuPreview() {
    MygosTheme {
        YaTienesTribuScreen(tribusIniciales[0], eventosIniciales[0], onVerMisTribus = {}, onVerOtrasTribus = {})
    }
}
