package com.example.mygos.ui.tribus

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.Publicacion
import com.example.mygos.Usuario
import com.example.mygos.data.publicacionesIniciales
import com.example.mygos.data.tribusIniciales
import com.example.mygos.ui.componentes.Avatar
import com.example.mygos.ui.componentes.AvisoPermiso
import com.example.mygos.ui.componentes.Cabecera
import com.example.mygos.ui.componentes.Subtitulo
import com.example.mygos.ui.componentes.Titulo
import com.example.mygos.ui.componentes.rayas
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.valeM
import com.example.mygos.viewmodel.MuroEstado
import com.example.mygos.viewmodel.MuroViewModel
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val insigniasTribu = listOf("Puntual", "Buena onda", "Anfitrión")

@Composable
fun MuroRuta(
    tribuId: Int,
    usuario: Usuario,
    onVolver: () -> Unit,
    viewModel: MuroViewModel = viewModel { MuroViewModel(tribuId, usuario) }
) {
    val context = LocalContext.current
    val estado by viewModel.estado.collectAsState()

    // Direccion del archivo donde la camara esta guardando la foto. rememberSaveable la conserva si Android
    // destruye la pantalla mientras la camara esta abierta
    var fotoPendiente by rememberSaveable { mutableStateOf<String?>(null) }
    var permisoNegado by remember { mutableStateOf(false) }

    // TakePicture abre la app de camara y avisa con true si la foto quedo guardada en el archivo
    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { guardada ->
        val foto = fotoPendiente
        if (guardada && foto != null) viewModel.publicarFoto(foto)
        fotoPendiente = null
    }

    val abrirCamara = {
        val uri = nuevoArchivoFoto(context)
        fotoPendiente = uri.toString()
        try {
            camara.launch(uri)
        } catch (e: ActivityNotFoundException) {
            fotoPendiente = null
            Toast.makeText(context, "No hay una app de cámara en este teléfono", Toast.LENGTH_SHORT).show()
        }
    }

    // Pide el permiso en tiempo de ejecucion. Solo si lo concede se abre la camara
    val permiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        permisoNegado = !concedido
        if (concedido) abrirCamara()
    }

    val onSubir = {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            abrirCamara()
        } else {
            permiso.launch(Manifest.permission.CAMERA)
        }
    }

    if (estado.tribu != null) {
        MuroScreen(
            estado = estado,
            usuario = usuario,
            permisoNegado = permisoNegado,
            onVolver = onVolver,
            onSubir = onSubir,
            onReintentarPermiso = { permiso.launch(Manifest.permission.CAMERA) },
            onInsignia = viewModel::alternarInsignia
        )
    }
}

// La foto se guarda en el cache de la app (carpeta declarada en provider_paths.xml).
// FileProvider la convierte en una direccion content:// que la camara puede escribir
private fun nuevoArchivoFoto(context: Context): Uri {
    val carpeta = File(context.cacheDir, "fotos").apply { mkdirs() }
    val archivo = File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
}

@Composable
fun MuroScreen(
    estado: MuroEstado,
    usuario: Usuario,
    permisoNegado: Boolean,
    onVolver: () -> Unit,
    onSubir: () -> Unit,
    onReintentarPermiso: () -> Unit,
    onInsignia: (Int, String) -> Unit
) {
    val tribu = estado.tribu ?: return
    val fotos = estado.publicaciones.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .navigationBarsPadding()
    ) {
        Cabecera(titulo = tribu.nombre, subtitulo = "${tribu.integrantes.size} personas · ${tribu.plan}", onVolver = onVolver)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) { Titulo("El", "muro") }
                Text(
                    if (fotos == 1) "1 foto" else "$fotos fotos",
                    color = TextoGris,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.height(14.dp))

            if (permisoNegado) {
                AvisoPermiso(
                    texto = "Mygos usa la cámara solo para que subas fotos al muro de tu tribu. Sin el permiso puedes ver el muro, pero no subir fotos.",
                    onReintentar = onReintentarPermiso
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // La primera casilla es el boton para subir; despues van las fotos de a tres por fila
            val casillas: List<Publicacion?> = listOf(null) + estado.publicaciones
            for (fila in casillas.chunked(3)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (publicacion in fila) {
                        val mod = Modifier
                            .weight(1f)
                            .aspectRatio(0.8f)
                        if (publicacion == null) {
                            CasillaSubir(onSubir, mod)
                        } else {
                            CasillaFoto(publicacion, mod)
                        }
                    }
                    // rellena la ultima fila para que las casillas no se estiren
                    repeat(3 - fila.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Subtitulo("Dale una insignia a tu tribu")
            Spacer(modifier = Modifier.height(12.dp))
            for (persona in tribu.integrantes.filter { it.id != usuario.id }) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp)) {
                    Avatar(usuario = persona, tamano = 34.dp, tamanoTexto = 12.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (insignia in insigniasTribu) {
                            ChipInsignia(
                                texto = insignia,
                                icono = iconoInsignia(insignia),
                                marcada = "${persona.id}:$insignia" in estado.insignias,
                                onClick = { onInsignia(persona.id, insignia) }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

private fun iconoInsignia(insignia: String): ImageVector = when (insignia) {
    "Puntual" -> Icons.Outlined.Schedule
    "Buena onda" -> Icons.Outlined.EmojiEmotions
    else -> Icons.Outlined.ConfirmationNumber
}

@Composable
fun CasillaSubir(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.AddAPhoto, contentDescription = null, tint = TextoClaro, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text("Subir", color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
    }
}

@Composable
fun CasillaFoto(publicacion: Publicacion, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(publicacion.autor.color)
            .rayas()
    ) {
        if (publicacion.foto != null) {
            val imagen = fotoDesdeUri(publicacion.foto)
            if (imagen != null) {
                Image(bitmap = imagen, contentDescription = "Foto de ${publicacion.autor.nombre}", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
        }
        Text(
            publicacion.autor.nombre,
            color = TextoClaro,
            fontFamily = Nunito,
            fontWeight = FontWeight.Black,
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        )
    }
}

// Lee la foto fuera del hilo principal para no trabar la pantalla.
// inSampleSize = 4 la carga a un cuarto del tamano: una casilla chica no necesita los 12 megapixeles
// shortcut: no se corrige la rotacion EXIF; usar ExifInterface si en un telefono real la foto sale girada
@Composable
fun fotoDesdeUri(direccion: String): ImageBitmap? {
    val context = LocalContext.current
    val imagen by produceState<ImageBitmap?>(null, direccion) {
        value = withContext(Dispatchers.IO) {
            val opciones = BitmapFactory.Options().apply { inSampleSize = 4 }
            context.contentResolver.openInputStream(Uri.parse(direccion))?.use { entrada ->
                BitmapFactory.decodeStream(entrada, null, opciones)?.asImageBitmap()
            }
        }
    }
    return imagen
}

@Composable
fun ChipInsignia(texto: String, icono: ImageVector, marcada: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .clip(CircleShape)
            .background(if (marcada) Naranja else Tarjeta)
            .border(1.dp, if (marcada) Naranja else Color.White.copy(alpha = 0.15f), CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = TextoClaro, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(5.dp))
        Text(texto, color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
    }
}

@Preview
@Composable
fun MuroPreview() {
    MygosTheme {
        MuroScreen(
            estado = MuroEstado(tribu = tribusIniciales[0], publicaciones = publicacionesIniciales.filter { it.tribuId == 1 }),
            usuario = valeM,
            permisoNegado = false,
            onVolver = {},
            onSubir = {},
            onReintentarPermiso = {},
            onInsignia = { _, _ -> }
        )
    }
}
