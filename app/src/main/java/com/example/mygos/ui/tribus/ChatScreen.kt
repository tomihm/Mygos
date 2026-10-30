package com.example.mygos.ui.tribus

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.Mensaje
import com.example.mygos.Usuario
import com.example.mygos.data.eventosIniciales
import com.example.mygos.data.mensajesIniciales
import com.example.mygos.data.tribusIniciales
import com.example.mygos.ui.componentes.BotonCircular
import com.example.mygos.ui.componentes.Cabecera
import com.example.mygos.ui.theme.BotonOscuro
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.valeM
import com.example.mygos.viewmodel.ChatEstado
import com.example.mygos.viewmodel.ChatViewModel

@Composable
fun ChatRuta(
    tribuId: Int,
    usuario: Usuario,
    onVolver: () -> Unit,
    onModoEncuentro: () -> Unit,
    onVerMuro: () -> Unit,
    viewModel: ChatViewModel = viewModel { ChatViewModel(tribuId, usuario) }
) {
    val estado by viewModel.estado.collectAsState()
    if (estado.tribu != null && estado.evento != null) {
        ChatScreen(
            estado = estado,
            usuario = usuario,
            onVolver = onVolver,
            onModoEncuentro = onModoEncuentro,
            onVerMuro = onVerMuro,
            onCambiarTexto = viewModel::cambiarTexto,
            onEnviar = viewModel::enviar
        )
    }
}

@Composable
fun ChatScreen(
    estado: ChatEstado,
    usuario: Usuario,
    onVolver: () -> Unit,
    onModoEncuentro: () -> Unit,
    onVerMuro: () -> Unit,
    onCambiarTexto: (String) -> Unit,
    onEnviar: () -> Unit
) {
    val tribu = estado.tribu ?: return
    val evento = estado.evento ?: return
    val lista = rememberLazyListState()

    // Cada vez que llega un mensaje nuevo la lista baja hasta el ultimo
    LaunchedEffect(estado.mensajes.size) {
        if (estado.mensajes.isNotEmpty()) lista.animateScrollToItem(estado.mensajes.size - 1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .navigationBarsPadding()
            .imePadding()
    ) {
        Cabecera(
            titulo = tribu.nombre,
            subtitulo = "${tribu.integrantes.size} personas · ${evento.titulo}",
            onVolver = onVolver,
            accion = { BotonCircular(icono = Icons.Filled.PhotoLibrary, descripcion = "Ver muro", onClick = onVerMuro) }
        )

        // Atajo al modo encuentro con el plan de la tribu
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Naranja)
                .padding(start = 14.dp, end = 10.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                Text("${evento.fecha.substringBefore(" ·")} · ${evento.hora}", color = Color.White, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 14.sp)
                Text(tribu.plan.replaceFirstChar { it.uppercase() }, color = Color.White, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable { onModoEncuentro() }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("Modo encuentro", color = Fondo, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 13.sp)
            }
        }

        LazyColumn(
            state = lista,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (estado.mensajes.isEmpty()) {
                item {
                    Text(
                        "Todavía no hay mensajes. ¡Saluda a tu tribu!",
                        color = TextoGris,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            items(estado.mensajes, key = { it.id }) { mensaje ->
                // animateItem: el mensaje nuevo entra con fundido y los demas se desplazan suave
                Burbuja(mensaje, propio = mensaje.autor.id == usuario.id, modifier = Modifier.animateItem())
            }
        }

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            OutlinedTextField(
                value = estado.texto,
                onValueChange = onCambiarTexto,
                placeholder = { Text("Escribe a la tribu...", fontFamily = Nunito, color = TextoGris) },
                isError = estado.error.isNotEmpty(),
                supportingText = if (estado.error.isNotEmpty()) {
                    { Text(estado.error, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                } else null,
                maxLines = 4,
                shape = RoundedCornerShape(22.dp),
                textStyle = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 15.sp),
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
                    errorSupportingTextColor = Naranja,
                    cursorColor = Naranja
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(8.dp))
            BotonCircular(
                icono = Icons.AutoMirrored.Filled.Send,
                descripcion = "Enviar",
                onClick = onEnviar,
                fondo = Naranja,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
fun Burbuja(mensaje: Mensaje, propio: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (propio) Alignment.End else Alignment.Start
    ) {
        if (!propio) {
            Text(
                mensaje.autor.nombre,
                color = mensaje.autor.color,
                fontFamily = Nunito,
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 3.dp)
            )
        }
        Text(
            text = mensaje.texto,
            color = TextoClaro,
            fontFamily = Nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(if (propio) Naranja else BotonOscuro)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        )
    }
}

@Preview
@Composable
fun ChatPreview() {
    MygosTheme {
        ChatScreen(
            estado = ChatEstado(tribu = tribusIniciales[0], evento = eventosIniciales[0], mensajes = mensajesIniciales.take(2)),
            usuario = valeM,
            onVolver = {},
            onModoEncuentro = {},
            onVerMuro = {},
            onCambiarTexto = {},
            onEnviar = {}
        )
    }
}
