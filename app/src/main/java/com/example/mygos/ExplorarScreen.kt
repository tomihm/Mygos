package com.example.mygos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygos.ui.theme.Amarillo
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.IconoGris
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

@Composable
fun ExplorarScreen(usuario: Usuario, onVerEvento: (Evento) -> Unit) {
    var categoria by remember { mutableStateOf("Todos") }

    val eventosFiltrados = if (categoria == "Todos") {
        eventos
    } else {
        eventos.filter { it.categoria == categoria }
    }

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
                    seleccionada = categoria,
                    onSeleccionar = { categoria = it }
                )
            }
            items(eventosFiltrados) { evento ->
                TarjetaEvento(evento, onClick = { onVerEvento(evento) })
            }
        }
        BarraInferior()
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
fun Filtros(seleccionada: String, onSeleccionar: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
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

@Composable
fun BarraInferior() {
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
            ItemBarra(Icons.Filled.LocalActivity, "Eventos", activo = true)
            ItemBarra(Icons.Outlined.Groups, "Tribus", activo = false, aviso = 2)
            ItemBarra(Icons.Outlined.Person, "Perfil", activo = false)
        }
    }
}

@Composable
fun ItemBarra(icono: ImageVector, texto: String, activo: Boolean, aviso: Int = 0) {
    val color = if (activo) Naranja else IconoGris
    Column(
        modifier = Modifier.width(72.dp),
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

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun ExplorarPreview() {
    MygosTheme {
        ExplorarScreen(usuario = valeM, onVerEvento = {})
    }
}
