package com.example.mygos.ui.eventos

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.Evento
import com.example.mygos.Usuario
import com.example.mygos.data.eventosIniciales
import com.example.mygos.productoraSonora
import com.example.mygos.ui.componentes.BotonNaranja
import com.example.mygos.ui.componentes.Cabecera
import com.example.mygos.ui.componentes.Titulo
import com.example.mygos.ui.theme.Amarillo
import com.example.mygos.ui.theme.AzulClaro
import com.example.mygos.ui.theme.BotonOscuro
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.ui.theme.Verde
import com.example.mygos.viewmodel.MisEventosViewModel

@Composable
fun MisEventosRuta(
    usuario: Usuario,
    onVolver: () -> Unit,
    onVerEvento: (Evento) -> Unit,
    onPublicar: () -> Unit,
    viewModel: MisEventosViewModel = viewModel { MisEventosViewModel(usuario.id) }
) {
    val eventos by viewModel.eventos.collectAsState()
    MisEventosScreen(usuario, eventos, onVolver, onVerEvento, onPublicar)
}

@Composable
fun MisEventosScreen(
    usuario: Usuario,
    eventos: List<Evento>,
    onVolver: () -> Unit,
    onVerEvento: (Evento) -> Unit,
    onPublicar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .navigationBarsPadding()
    ) {
        Cabecera(titulo = usuario.nombre, subtitulo = "Organizador", onVolver = onVolver)
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Titulo("Mis", "eventos")
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CajaResumen(eventos.sumOf { it.buscanGrupo }, "Personas buscando tribu", Amarillo, Fondo, Modifier.weight(1f))
                    CajaResumen(eventos.sumOf { it.tribus }, "Tribus formadas", AzulClaro, Color.White, Modifier.weight(1f))
                }
            }
            if (eventos.isEmpty()) {
                item {
                    Text(
                        "Todavía no publicas eventos. Publica uno y aparecerá en Explorar para que la gente arme tribus.",
                        color = TextoSuave,
                        fontFamily = Nunito,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
            items(eventos, key = { it.id }) { evento ->
                TarjetaMiEvento(evento, onClick = { onVerEvento(evento) }, modifier = Modifier.animateItem())
            }
        }
        BotonNaranja(
            texto = "Publicar evento",
            onClick = onPublicar,
            icono = Icons.Filled.Add,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 12.dp)
        )
    }
}

@Composable
fun CajaResumen(numero: Int, texto: String, fondo: Color, colorTexto: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(110.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(fondo)
            .padding(14.dp)
    ) {
        Text("$numero", color = colorTexto, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 34.sp)
        Text(texto, color = colorTexto, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, lineHeight = 15.sp)
    }
}

@Composable
fun TarjetaMiEvento(evento: Evento, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Tarjeta)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row {
            Image(
                painter = painterResource(evento.foto),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(evento.titulo, color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 16.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text("${evento.fecha} · ${evento.hora}", color = TextoGris, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Verde.copy(alpha = 0.18f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("Publicado", color = Verde, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DatoEvento(evento.asistentes.size, "asisten", TextoClaro, Modifier.weight(1f))
            DatoEvento(evento.tribus, "tribus", Naranja, Modifier.weight(1f))
            DatoEvento(evento.buscanGrupo, "sin tribu", Verde, Modifier.weight(1f))
        }
    }
}

@Composable
fun DatoEvento(numero: Int, texto: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BotonOscuro)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text("$numero", color = color, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 18.sp)
        Text(texto, color = TextoGris, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 11.sp)
    }
}

@Preview
@Composable
fun MisEventosPreview() {
    MygosTheme {
        MisEventosScreen(productoraSonora, eventosIniciales.filter { it.organizador == productoraSonora }, {}, {}, {})
    }
}
