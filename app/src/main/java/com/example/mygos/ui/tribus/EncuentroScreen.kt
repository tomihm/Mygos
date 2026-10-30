package com.example.mygos.ui.tribus

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.Usuario
import com.example.mygos.data.eventosIniciales
import com.example.mygos.data.tribusIniciales
import com.example.mygos.ui.componentes.Avatar
import com.example.mygos.ui.componentes.AvisoPermiso
import com.example.mygos.ui.componentes.BotonContorno
import com.example.mygos.ui.componentes.BotonNaranja
import com.example.mygos.ui.componentes.Cabecera
import com.example.mygos.ui.componentes.Subtitulo
import com.example.mygos.ui.componentes.rayas
import com.example.mygos.ui.theme.Amarillo
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.ui.theme.Verde
import com.example.mygos.ui.theme.VerdeBosque
import com.example.mygos.valeM
import com.example.mygos.viewmodel.EncuentroEstado
import com.example.mygos.viewmodel.EncuentroViewModel
import com.example.mygos.viewmodel.RADIO_LLEGADA
import com.example.mygos.viewmodel.textoDistancia
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

private val permisosUbicacion = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

@Composable
fun EncuentroRuta(
    tribuId: Int,
    usuario: Usuario,
    onVolver: () -> Unit,
    viewModel: EncuentroViewModel = viewModel { EncuentroViewModel(tribuId) }
) {
    val context = LocalContext.current
    val estado by viewModel.estado.collectAsState()

    val pedirUbicacion = {
        viewModel.permisoConcedido()
        leerUbicacion(context, onUbicacion = viewModel::ubicacionRecibida, onSinUbicacion = viewModel::ubicacionNoDisponible)
    }

    // Basta con que conceda una de las dos: la aproximada tambien sirve para medir la distancia
    val permiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { respuesta ->
        if (respuesta.values.any { it }) pedirUbicacion() else viewModel.permisoNegado()
    }

    // Al abrir la pantalla: si ya tiene permiso lee la ubicacion; si no, lo pide
    LaunchedEffect(Unit) {
        if (tienePermisoUbicacion(context)) pedirUbicacion() else permiso.launch(permisosUbicacion)
    }

    LaunchedEffect(estado.error) {
        if (estado.error.isNotEmpty()) {
            Toast.makeText(context, estado.error, Toast.LENGTH_SHORT).show()
            viewModel.errorMostrado()
        }
    }

    if (estado.tribu != null && estado.evento != null) {
        EncuentroScreen(
            estado = estado,
            usuario = usuario,
            onVolver = onVolver,
            onReintentar = { permiso.launch(permisosUbicacion) },
            onActualizar = { if (tienePermisoUbicacion(context)) pedirUbicacion() else permiso.launch(permisosUbicacion) },
            onComoLlegar = { abrirMapa(context, estado) },
            onYaLlegue = viewModel::yaLlegue
        )
    }
}

private fun tienePermisoUbicacion(context: Context): Boolean {
    return permisosUbicacion.any { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
}

// FusedLocationProviderClient (Google Play Services) mezcla GPS, wifi y antenas para dar la ubicacion.
// getCurrentLocation pide una lectura nueva en vez de usar la ultima guardada
@SuppressLint("MissingPermission") // solo se llama despues de revisar el permiso
private fun leerUbicacion(context: Context, onUbicacion: (Double, Double) -> Unit, onSinUbicacion: () -> Unit) {
    LocationServices.getFusedLocationProviderClient(context)
        .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
        .addOnSuccessListener { ubicacion ->
            if (ubicacion != null) onUbicacion(ubicacion.latitude, ubicacion.longitude) else onSinUbicacion()
        }
        .addOnFailureListener { onSinUbicacion() }
}

// Abre Google Maps (o la app de mapas que tenga el telefono) con el lugar del evento. No necesita API key
private fun abrirMapa(context: Context, estado: EncuentroEstado) {
    val evento = estado.evento ?: return
    val uri = Uri.parse("geo:${evento.latitud},${evento.longitud}?q=${evento.latitud},${evento.longitud}(${Uri.encode(evento.lugar)})")
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No hay una app de mapas instalada", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun EncuentroScreen(
    estado: EncuentroEstado,
    usuario: Usuario,
    onVolver: () -> Unit,
    onReintentar: () -> Unit,
    onActualizar: () -> Unit,
    onComoLlegar: () -> Unit,
    onYaLlegue: () -> Unit
) {
    val tribu = estado.tribu ?: return
    val evento = estado.evento ?: return
    val distancia = estado.distancia

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .navigationBarsPadding()
    ) {
        Cabecera(titulo = "Modo encuentro · ${tribu.nombre}", subtitulo = evento.lugar, onVolver = onVolver)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            PuntoDeEncuentro(tribu.plan)
            Spacer(modifier = Modifier.height(14.dp))

            when {
                estado.sinPermiso -> AvisoPermiso(
                    texto = "Para decirte cuánto te falta y confirmar que llegaste, Mygos necesita tu ubicación. Solo se usa en esta pantalla y no se guarda.",
                    onReintentar = onReintentar
                )
                estado.buscando -> FilaEstado(Icons.Filled.MyLocation, "Buscando tu ubicación...", TextoSuave)
                distancia != null -> FilaEstado(
                    icono = Icons.Filled.MyLocation,
                    texto = if (distancia <= RADIO_LLEGADA) "¡Estás en el lugar!" else "Estás a ${textoDistancia(distancia)} del lugar",
                    color = if (distancia <= RADIO_LLEGADA) Verde else TextoClaro,
                    accion = "Actualizar",
                    onAccion = onActualizar
                )
                else -> FilaEstado(Icons.Filled.MyLocation, "Sin ubicación todavía", TextoSuave, accion = "Reintentar", onAccion = onActualizar)
            }

            Spacer(modifier = Modifier.height(10.dp))
            BotonContorno(texto = "Cómo llegar", onClick = onComoLlegar, icono = Icons.Filled.Directions)

            Spacer(modifier = Modifier.height(22.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) { Subtitulo("¿Quién llegó?") }
                val llegaron = if (estado.llegue) 1 else 0
                Text("$llegaron de ${tribu.integrantes.size}", color = Verde, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            for (persona in tribu.integrantes) {
                val soyYo = persona.id == usuario.id
                FilaLlegada(persona, soyYo, llego = soyYo && estado.llegue, enCamino = soyYo && distancia != null)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 12.dp)) {
            if (estado.llegue) {
                BotonContorno(texto = "Llegada confirmada", onClick = {}, icono = Icons.Filled.CheckCircle, colorTexto = Verde, colorBorde = Verde)
            } else {
                BotonNaranja(texto = "Ya llegué", onClick = onYaLlegue, icono = Icons.Filled.LocationOn)
            }
        }
    }
}

// "Mapa" del punto de encuentro: el pin late con una onda que crece y se desvanece, sin parar
@Composable
fun PuntoDeEncuentro(plan: String) {
    val pulso = rememberInfiniteTransition(label = "pulso")
    val escala by pulso.animateFloat(
        initialValue = 0.6f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing), RepeatMode.Restart),
        label = "escalaPulso"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(VerdeBosque.copy(alpha = 0.45f))
            .rayas(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .graphicsLayer {
                    scaleX = escala
                    scaleY = escala
                    // la onda se apaga mientras crece
                    alpha = (2.2f - escala) / 1.6f
                }
                .clip(CircleShape)
                .background(Naranja.copy(alpha = 0.5f))
        )
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(Naranja)
        )
        Text(
            plan.replaceFirstChar { it.uppercase() },
            color = Color.White,
            fontFamily = Nunito,
            fontWeight = FontWeight.Black,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 22.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Naranja)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun FilaEstado(icono: ImageVector, texto: String, color: Color, accion: String = "", onAccion: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Tarjeta)
            .padding(start = 14.dp, end = 6.dp, top = 6.dp, bottom = 6.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(texto, color = color, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, modifier = Modifier.weight(1f))
        if (accion.isNotEmpty()) {
            BotonContorno(texto = accion, onClick = onAccion, modifier = Modifier.width(110.dp).height(36.dp))
        }
    }
}

@Composable
fun FilaLlegada(persona: Usuario, soyYo: Boolean, llego: Boolean, enCamino: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Tarjeta)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(usuario = persona, tamano = 32.dp, tamanoTexto = 11.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            if (soyYo) "Tú" else persona.nombre,
            color = TextoClaro,
            fontFamily = Nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        val (icono, texto, color) = when {
            llego -> Triple(Icons.Filled.CheckCircle, "Llegó", Verde)
            enCamino -> Triple(Icons.AutoMirrored.Filled.DirectionsWalk, "En camino", Amarillo)
            else -> Triple(Icons.Outlined.HelpOutline, "Sin confirmar", TextoGris)
        }
        Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(texto, color = color, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
    }
}

@Preview
@Composable
fun EncuentroPreview() {
    MygosTheme {
        EncuentroScreen(
            estado = EncuentroEstado(tribu = tribusIniciales[1], evento = eventosIniciales[0], latitud = -33.44, longitud = -70.645),
            usuario = valeM,
            onVolver = {},
            onReintentar = {},
            onActualizar = {},
            onComoLlegar = {},
            onYaLlegue = {}
        )
    }
}
