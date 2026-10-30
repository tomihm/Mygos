package com.example.mygos.ui.eventos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.Evento
import com.example.mygos.Usuario
import com.example.mygos.data.eventosIniciales
import com.example.mygos.ui.componentes.Avatar
import com.example.mygos.ui.componentes.BarraInferior
import com.example.mygos.ui.componentes.Filtros
import com.example.mygos.ui.componentes.Seccion
import com.example.mygos.ui.theme.Amarillo
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.InstrumentSerif
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.ui.theme.TextoTarjeta
import com.example.mygos.ui.theme.Verde
import com.example.mygos.valeM
import com.example.mygos.viewmodel.ExplorarEstado
import com.example.mygos.viewmodel.ExplorarViewModel

@Composable
fun ExplorarRuta(
    usuario: Usuario,
    onVerEvento: (Evento) -> Unit,
    onIrASeccion: (Seccion) -> Unit,
    viewModel: ExplorarViewModel = viewModel { ExplorarViewModel(usuario.id) }
) {
    val estado by viewModel.estado.collectAsState()
    ExplorarScreen(
        usuario = usuario,
        estado = estado,
        onElegirCategoria = viewModel::elegirCategoria,
        onVerEvento = onVerEvento,
        onIrASeccion = onIrASeccion
    )
}

@Composable
fun ExplorarScreen(
    usuario: Usuario,
    estado: ExplorarEstado,
    onElegirCategoria: (String) -> Unit,
    onVerEvento: (Evento) -> Unit,
    onIrASeccion: (Seccion) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Encabezado(usuario)
            }
            item {
                Filtros(
                    categorias = estado.categorias,
                    seleccionada = estado.categoria,
                    onSeleccionar = onElegirCategoria
                )
            }
            items(estado.eventos) { evento ->
                TarjetaEvento(evento, onClick = { onVerEvento(evento) })
            }
        }
        BarraInferior(actual = Seccion.EVENTOS, misTribus = estado.misTribus, onIr = onIrASeccion)
    }
}

@Composable
fun Encabezado(usuario: Usuario) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hola, ${usuario.nombre}",
                color = TextoSuave,
                fontFamily = Nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 32.sp)) {
                        append("Explorar ")
                    }
                    withStyle(SpanStyle(fontFamily = InstrumentSerif, fontSize = 40.sp)) {
                        append("eventos")
                    }
                },
                color = TextoClaro,
                letterSpacing = (-0.64).sp
            )
        }
        Avatar(
            usuario = usuario,
            tamano = 46.dp,
            tamanoTexto = 14.sp,
            borde = 2.dp,
            colorBorde = Naranja,
            rayado = true,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}


@Composable
fun TarjetaEvento(evento: Evento, onClick: () -> Unit) {
    val forma = RoundedCornerShape(26.dp)
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(forma)
            .background(Tarjeta)
            .border(1.dp, Color.White.copy(alpha = 0.06f), forma)
            .clickable { onClick() }
    ) {
        // Parte de arriba: foto del evento con un degradado para que se lea el texto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(196.dp)
        ) {
            Image(
                painter = painterResource(evento.foto),
                contentDescription = evento.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Black.copy(alpha = 0.15f),
                            0.3f to Color.Transparent,
                            1f to Color(0xF00A0908)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .size(width = 52.dp, height = 47.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(TextoClaro),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = evento.mes,
                    color = Naranja,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    lineHeight = 11.sp
                )
                Text(
                    text = evento.dia,
                    color = Fondo,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    lineHeight = 22.sp
                )
            }

            val textoEtiqueta = if (evento.yaTienesTribu) "Ya tienes tribu" else evento.categoria
            val fondoEtiqueta = if (evento.yaTienesTribu) Verde else Fondo.copy(alpha = 0.6f)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(14.dp)
                    .height(28.dp)
                    .clip(CircleShape)
                    .background(fondoEtiqueta)
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = textoEtiqueta,
                    color = TextoClaro,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
            ) {
                Text(
                    text = evento.titulo,
                    color = TextoClaro,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    lineHeight = 24.2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${evento.organizador.nombre} · ${evento.hora}",
                    color = TextoTarjeta,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(start = 16.dp, end = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                evento.asistentes.forEachIndexed { i, persona ->
                    Avatar(
                        usuario = persona,
                        tamano = 28.dp,
                        tamanoTexto = 10.sp,
                        borde = 2.dp,
                        colorBorde = Tarjeta,
                        modifier = Modifier.offset(x = (20 * i).dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width((20 * (evento.asistentes.size - 1) + 10).dp))
            val texto = if (evento.tribus == 1) "1 tribu formándose" else "${evento.tribus} tribus formándose"
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Amarillo)
                    .padding(horizontal = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = texto,
                    color = Fondo,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ver evento",
                tint = TextoGris,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}




@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun ExplorarPreview() {
    MygosTheme {
        ExplorarScreen(
            usuario = valeM,
            estado = ExplorarEstado(eventos = eventosIniciales),
            onElegirCategoria = {},
            onVerEvento = {},
            onIrASeccion = {}
        )
    }
}
