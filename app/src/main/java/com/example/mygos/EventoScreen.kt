package com.example.mygos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygos.ui.theme.Amarillo
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoDescripcion
import com.example.mygos.ui.theme.TextoSuave

@Composable
fun EventoScreen(evento: Evento, onVolver: () -> Unit, onBuscarTribu: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
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
                BotonCircular(
                    icono = Icons.AutoMirrored.Filled.ArrowBack,
                    descripcion = "Volver",
                    onClick = onVolver,
                    fondo = Fondo.copy(alpha = 0.55f),
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(start = 16.dp, top = 10.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp, bottom = 44.dp)
                        .height(28.dp)
                        .clip(CircleShape)
                        .background(Naranja)
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = evento.categoria,
                        color = TextoClaro,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }
            }

            // la hoja de abajo se sube un poco sobre la foto
            Column(
                modifier = Modifier
                    .offset(y = (-28).dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Fondo)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = evento.titulo,
                    color = TextoClaro,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Black,
                    fontSize = 30.sp,
                    lineHeight = 31.5.sp,
                    letterSpacing = (-0.6).sp,
                    modifier = Modifier.padding(end = 70.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Tarjeta)
                ) {
                    FilaInfo(icono = Icons.Outlined.CalendarMonth, titulo = evento.fecha, detalle = evento.horario)
                    HorizontalDivider(color = Color.White.copy(alpha = 0.06f), thickness = 1.dp)
                    FilaInfo(icono = Icons.Outlined.LocationOn, titulo = evento.lugar, detalle = evento.detalleLugar)
                    HorizontalDivider(color = Color.White.copy(alpha = 0.06f), thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(67.dp)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Avatar(usuario = evento.organizador, tamano = 22.dp, tamanoTexto = 10.sp)
                        Spacer(modifier = Modifier.width(14.dp))
                        TextosInfo(titulo = evento.organizador.nombre, detalle = "Organizador")
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = evento.descripcion,
                    color = TextoDescripcion,
                    fontFamily = Nunito,
                    fontSize = 15.sp,
                    lineHeight = 22.5.sp
                )

                Spacer(modifier = Modifier.height(18.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Amarillo)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Groups,
                            contentDescription = null,
                            tint = Fondo,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TRIBUS DE ESTE EVENTO",
                            color = Fondo,
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            letterSpacing = 0.72.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    val textoTribus = if (evento.tribus == 1) "1 tribu formándose" else "${evento.tribus} tribus formándose"
                    Text(
                        text = "$textoTribus · ${evento.buscanGrupo} personas buscan grupo",
                        color = Fondo,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        lineHeight = 21.6.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Te mostramos primero las que más se parecen a ti.",
                        color = Fondo,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                // espacio para que el boton de abajo no tape la tarjeta amarilla
                Spacer(modifier = Modifier.height(110.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0f to Fondo.copy(alpha = 0f),
                        0.35f to Fondo
                    )
                )
                .navigationBarsPadding()
                .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 12.dp)
        ) {
            BotonNaranja(
                texto = "Encontrar mi tribu",
                icono = Icons.Filled.GroupAdd,
                onClick = onBuscarTribu
            )
        }
    }
}

@Composable
fun FilaInfo(icono: ImageVector, titulo: String, detalle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(67.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = Naranja,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        TextosInfo(titulo = titulo, detalle = detalle)
    }
}

@Composable
fun TextosInfo(titulo: String, detalle: String) {
    Column {
        Text(
            text = titulo,
            color = TextoClaro,
            fontFamily = Nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp
        )
        Text(
            text = detalle,
            color = TextoSuave,
            fontFamily = Nunito,
            fontSize = 13.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun EventoPreview() {
    MygosTheme {
        EventoScreen(evento = eventos[0], onVolver = {}, onBuscarTribu = {})
    }
}
