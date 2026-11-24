package com.example.mygos.ui.foro

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.Usuario
import com.example.mygos.remote.PublicacionForo
import com.example.mygos.ui.componentes.BotonContorno
import com.example.mygos.ui.componentes.BotonNaranja
import com.example.mygos.ui.componentes.Cabecera
import com.example.mygos.ui.componentes.CampoTexto
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoDescripcion
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.viewmodel.ForoEstado
import com.example.mygos.viewmodel.ForoViewModel

@Composable
fun ForoRuta(usuario: Usuario, onVolver: () -> Unit, viewModel: ForoViewModel = viewModel { ForoViewModel(usuario.nombre) }) {
    val estado by viewModel.estado.collectAsState()
    ForoScreen(
        estado = estado,
        onVolver = onVolver,
        onReintentar = viewModel::cargar,
        onCambiarTitulo = viewModel::cambiarTitulo,
        onCambiarCuerpo = viewModel::cambiarCuerpo,
        onGuardar = viewModel::guardar,
        onCancelarEdicion = viewModel::cancelarEdicion,
        onEditar = viewModel::empezarEdicion,
        onBorrar = viewModel::borrar,
        onAbrir = viewModel::abrir,
        onCerrar = viewModel::cerrar,
        onCambiarComentario = viewModel::cambiarComentario,
        onComentar = viewModel::comentar
    )
}

@Composable
fun ForoScreen(
    estado: ForoEstado,
    onVolver: () -> Unit,
    onReintentar: () -> Unit,
    onCambiarTitulo: (String) -> Unit,
    onCambiarCuerpo: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelarEdicion: () -> Unit,
    onEditar: (PublicacionForo) -> Unit,
    onBorrar: (PublicacionForo) -> Unit,
    onAbrir: (PublicacionForo) -> Unit,
    onCerrar: () -> Unit,
    onCambiarComentario: (String) -> Unit,
    onComentar: () -> Unit
) {
    val foco = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
    ) {
        Cabecera(titulo = "Foro de la comunidad", subtitulo = "Ideas y recomendaciones de otros usuarios", onVolver = onVolver)

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CampoTexto(estado.titulo, onCambiarTitulo, "Título", Icons.Filled.Title, error = estado.errorTitulo)
                    Spacer(modifier = Modifier.height(8.dp))
                    CampoTexto(estado.cuerpo, onCambiarCuerpo, "¿Qué quieres contar?", Icons.Filled.Edit, error = estado.errorCuerpo, lineas = 3)
                    Spacer(modifier = Modifier.height(10.dp))
                    BotonNaranja(
                        texto = when {
                            estado.enviando -> "Enviando..."
                            estado.editandoId != null -> "Guardar cambios"
                            else -> "Publicar"
                        },
                        onClick = {
                            // baja el teclado para que se vea la lista con la publicacion nueva
                            foco.clearFocus()
                            if (!estado.enviando) onGuardar()
                        },
                        icono = Icons.Filled.Send
                    )
                    if (estado.editandoId != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        BotonContorno(texto = "Cancelar edición", onClick = onCancelarEdicion)
                    }
                    if (estado.error.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(estado.error, color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        TextButton(onClick = onReintentar) {
                            Text("Reintentar", color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
            if (estado.cargando) {
                item {
                    CircularProgressIndicator(color = Naranja, modifier = Modifier.padding(30.dp))
                }
            }
            items(estado.publicaciones, key = { it.id ?: 0 }) { publicacion ->
                TarjetaPublicacion(
                    publicacion = publicacion,
                    onAbrir = { onAbrir(publicacion) },
                    onEditar = { onEditar(publicacion) },
                    onBorrar = { onBorrar(publicacion) }
                )
            }
            item { Spacer(modifier = Modifier.navigationBarsPadding().height(10.dp)) }
        }
    }

    val abierta = estado.abierta
    if (abierta != null) {
        AlertDialog(
            onDismissRequest = onCerrar,
            title = { Text(abierta.titulo, fontFamily = Nunito, fontWeight = FontWeight.Black) },
            text = {
                Column(modifier = Modifier.heightIn(max = 420.dp).verticalScroll(rememberScrollState())) {
                    Text("Por ${abierta.autor}", color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(abierta.cuerpo, fontFamily = Nunito)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Comentarios (${estado.comentarios.size})", color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.Black)
                    estado.comentarios.forEach { comentario ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(comentario.autor, color = TextoGris, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(comentario.texto, fontFamily = Nunito, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    CampoTexto(estado.comentario, onCambiarComentario, "Escribe un comentario", Icons.Filled.Edit, error = estado.errorComentario)
                    TextButton(onClick = onComentar) {
                        Text("Comentar", color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onCerrar) {
                    Text("Cerrar", color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
                }
            },
            containerColor = Tarjeta,
            titleContentColor = TextoClaro,
            textContentColor = TextoDescripcion
        )
    }
}

@Composable
fun TarjetaPublicacion(publicacion: PublicacionForo, onAbrir: () -> Unit, onEditar: () -> Unit, onBorrar: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Tarjeta)
            .clickable { onAbrir() }
            .padding(14.dp)
    ) {
        Text(publicacion.titulo, color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 15.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(publicacion.autor, color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(publicacion.cuerpo, color = TextoSuave, fontFamily = Nunito, fontSize = 13.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onEditar) {
                Text("Editar", color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
            }
            TextButton(onClick = onBorrar) {
                Text("Borrar", color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Preview
@Composable
fun ForoPreview() {
    MygosTheme {
        ForoScreen(
            ForoEstado(cargando = false, publicaciones = listOf(PublicacionForo(1, "Mejor café cerca del parque", "Vayan temprano que se llena", "Vale_M"))),
            {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}
        )
    }
}
