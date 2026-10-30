package com.example.mygos.ui.tribus

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.Evento
import com.example.mygos.Tribu
import com.example.mygos.Usuario
import com.example.mygos.data.eventosIniciales
import com.example.mygos.data.tribusIniciales
import com.example.mygos.ui.componentes.BotonCircular
import com.example.mygos.ui.componentes.BotonNaranja
import com.example.mygos.ui.componentes.rayas
import com.example.mygos.ui.cuenta.TextoEnlace
import com.example.mygos.ui.theme.Amarillo
import com.example.mygos.ui.theme.Azul
import com.example.mygos.ui.theme.BotonOscuro
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoDescripcion
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.ui.theme.Verde
import com.example.mygos.viewmodel.TribusEstado
import com.example.mygos.viewmodel.TribusViewModel
import kotlin.math.abs
import kotlinx.coroutines.launch

@Composable
fun TribusRuta(
    eventoId: Int,
    usuario: Usuario,
    onVolver: () -> Unit,
    onUnirse: (Tribu) -> Unit,
    onVerTribu: (Tribu) -> Unit,
    viewModel: TribusViewModel = viewModel { TribusViewModel(eventoId) }
) {
    val context = LocalContext.current
    val estado by viewModel.estado.collectAsState()

    LaunchedEffect(estado.unidoA) {
        estado.unidoA?.let {
            onUnirse(it)
            viewModel.unionMostrada()
        }
    }
    LaunchedEffect(estado.error) {
        if (estado.error.isNotEmpty()) {
            Toast.makeText(context, estado.error, Toast.LENGTH_SHORT).show()
            viewModel.errorMostrado()
        }
    }

    // mientras el evento no llega no se dibuja nada
    val evento = estado.evento
    if (evento != null) {
        TribusScreen(
            evento = evento,
            estado = estado,
            onSaltar = viewModel::saltar,
            onUnirme = { viewModel.unirse(usuario) },
            onVerDeNuevo = viewModel::verDeNuevo,
            onVolver = onVolver,
            onVerTribu = onVerTribu
        )
    }
}

@Composable
fun TribusScreen(
    evento: Evento,
    estado: TribusEstado,
    onSaltar: () -> Unit,
    onUnirme: () -> Unit,
    onVerDeNuevo: () -> Unit,
    onVolver: () -> Unit,
    onVerTribu: (Tribu) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonCircular(Icons.AutoMirrored.Filled.ArrowBack, "Volver", onClick = onVolver)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "TRIBUS PARA TI",
                    color = TextoGris,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    letterSpacing = 0.72.sp
                )
                Text(
                    text = evento.titulo,
                    color = TextoClaro,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // una barrita por cada tribu, como las historias
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in estado.tribus.indices) {
                val color = if (i <= estado.indice) TextoClaro else Color.White.copy(alpha = 0.18f)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        val tribu = estado.tribuActual
        if (tribu != null) {
            // desliz va de -1 (sale por la izquierda: salto) a 1 (sale por la derecha: me uno). 0 = al centro
            val desliz = remember { Animatable(0f) }
            val entrada = remember { Animatable(1f) }
            val scope = rememberCoroutineScope()
            val vibracion = LocalHapticFeedback.current

            // Cada tribu nueva entra creciendo desde atras con un rebote
            LaunchedEffect(tribu.id) {
                desliz.snapTo(0f)
                entrada.snapTo(0.85f)
                entrada.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            }

            val saltar: () -> Unit = {
                scope.launch {
                    desliz.animateTo(-1f, tween(280))
                    onSaltar()
                }
            }
            val unirme: () -> Unit = {
                vibracion.performHapticFeedback(HapticFeedbackType.LongPress)
                scope.launch {
                    desliz.animateTo(1f, tween(320))
                    onUnirme()
                    // si no se pudo unir (llena o ya tiene tribu) la tarjeta vuelve a su lugar
                    desliz.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                TarjetaTribu(
                    tribu = tribu,
                    onInfo = { onVerTribu(tribu) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 14.dp, end = 14.dp, bottom = 44.dp)
                        .graphicsLayer {
                            translationX = desliz.value * size.width
                            rotationZ = desliz.value * 14f
                            scaleX = entrada.value
                            scaleY = entrada.value
                        }
                )
                // Sello que aparece mientras la tarjeta se va: verde al unirse, gris al saltar
                if (desliz.value != 0f) {
                    val meUno = desliz.value > 0
                    Text(
                        text = if (meUno) "¡ME UNO!" else "PASO",
                        color = if (meUno) Verde else TextoClaro,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Black,
                        fontSize = 34.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .graphicsLayer {
                                alpha = abs(desliz.value)
                                rotationZ = if (meUno) -12f else 12f
                            }
                            .border(4.dp, if (meUno) Verde else TextoClaro, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(74.dp)
                            .shadow(14.dp, CircleShape)
                            .clip(CircleShape)
                            .background(TextoClaro)
                            .clickable { saltar() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Saltar tribu",
                            tint = Azul,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .width(154.dp)
                            .height(74.dp)
                            .shadow(16.dp, CircleShape, spotColor = Naranja, ambientColor = Naranja)
                            .clip(CircleShape)
                            .background(Naranja)
                            .clickable { unirme() },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.GroupAdd,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Unirme",
                            color = Color.White,
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp
                        )
                    }
                }
            }
        } else {
            SinMasTribus(onVolverAEmpezar = onVerDeNuevo, onVolver = onVolver)
        }
    }
}

@Composable
fun TarjetaTribu(tribu: Tribu, onInfo: () -> Unit, modifier: Modifier = Modifier) {
    val cuposLibres = tribu.cupos - tribu.integrantes.size
    val textoCupos = if (cuposLibres == 1) "queda 1 cupo" else "quedan $cuposLibres cupos"

    Column(
        modifier = modifier
            .shadow(20.dp, RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(30.dp))
            .background(Tarjeta)
    ) {
        Box {
            GrillaIntegrantes(tribu.integrantes)
            ChipAfinidad("${tribu.afinidad}% afinidad", Modifier.padding(14.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(14.dp)
                    .height(32.dp)
                    .clip(CircleShape)
                    .background(Fondo.copy(alpha = 0.65f))
                    .padding(horizontal = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Verified,
                    contentDescription = null,
                    tint = Verde,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Todos verificados",
                    color = TextoClaro,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
            }
        }

        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = tribu.nombre,
                    color = TextoClaro,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp,
                    lineHeight = 28.sp,
                    letterSpacing = (-0.56).sp,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        .clickable { onInfo() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Ver integrantes",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${tribu.integrantes.size} personas · $textoCupos",
                color = TextoSuave,
                fontFamily = Nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (interes in tribu.intereses) {
                    ChipInteres(interes)
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.EventNote,
                    contentDescription = null,
                    tint = Naranja,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Plan:  ${tribu.plan}",
                    color = TextoDescripcion,
                    fontFamily = Nunito,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// Se muestran hasta 4 integrantes; si faltan, el cuadro queda como cupo libre
@Composable
fun GrillaIntegrantes(integrantes: List<Usuario>, modifier: Modifier = Modifier, conNombres: Boolean = true) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        for (fila in 0..1) {
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                for (columna in 0..1) {
                    val persona = integrantes.getOrNull(fila * 2 + columna)
                    CuadroIntegrante(persona, Modifier.weight(1f), conNombre = conNombres)
                }
            }
        }
    }
}

@Composable
fun ChipAfinidad(texto: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(33.dp)
            .clip(CircleShape)
            .background(Amarillo)
            .padding(horizontal = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Bolt,
            contentDescription = null,
            tint = Fondo,
            modifier = Modifier.size(17.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = texto,
            color = Fondo,
            fontFamily = Nunito,
            fontWeight = FontWeight.Black,
            fontSize = 13.sp
        )
    }
}

@Composable
fun ChipInteres(texto: String) {
    Box(
        modifier = Modifier
            .height(26.dp)
            .clip(CircleShape)
            .background(Naranja.copy(alpha = 0.22f))
            .border(1.dp, Naranja, CircleShape)
            .padding(horizontal = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = TextoClaro,
            fontFamily = Nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp
        )
    }
}

@Composable
fun CuadroIntegrante(persona: Usuario?, modifier: Modifier = Modifier, conNombre: Boolean = true) {
    val fondo = persona?.color ?: BotonOscuro
    Box(
        modifier = modifier
            .height(148.dp)
            .background(fondo)
            .rayas()
    ) {
        if (persona?.foto != null) {
            Image(
                painter = painterResource(persona.foto),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        // degradado abajo para que el nombre se lea sobre la foto
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Brush.verticalGradient(0.5f to Color.Transparent, 1f to Color.Black.copy(alpha = 0.45f)))
        )
        if (conNombre) {
            Text(
                text = persona?.nombre ?: "Cupo libre",
                color = if (persona == null) TextoGris else TextoClaro,
                fontFamily = Nunito,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp,
                style = TextStyle(shadow = Shadow(Color.Black.copy(alpha = 0.6f), Offset(0f, 2f), 8f)),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 10.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun SinMasTribus(onVolverAEmpezar: () -> Unit, onVolver: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ya viste todas las tribus",
            color = TextoClaro,
            fontFamily = Nunito,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Puedes revisarlas otra vez o volver al evento.",
            color = TextoSuave,
            fontFamily = Nunito,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        BotonNaranja(texto = "Ver de nuevo", onClick = onVolverAEmpezar)
        Spacer(modifier = Modifier.height(14.dp))
        TextoEnlace("", "Volver al evento", onClick = onVolver)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun TribusPreview() {
    MygosTheme {
        TribusScreen(
            evento = eventosIniciales[0],
            estado = TribusEstado(tribus = tribusIniciales.filter { it.eventoId == 1 }),
            onSaltar = {},
            onUnirme = {},
            onVerDeNuevo = {},
            onVolver = {},
            onVerTribu = {}
        )
    }
}
