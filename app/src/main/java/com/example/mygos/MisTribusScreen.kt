package com.example.mygos

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.data.eventosIniciales
import com.example.mygos.data.tribusIniciales
import com.example.mygos.ui.theme.Amarillo
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.IconoGris
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.viewmodel.MisTribusViewModel
import com.example.mygos.viewmodel.TribuConEvento

@Composable
fun MisTribusRuta(
    usuario: Usuario,
    onVerTribu: (Tribu) -> Unit,
    onIrASeccion: (Seccion) -> Unit,
    viewModel: MisTribusViewModel = viewModel { MisTribusViewModel(usuario.id) }
) {
    val tribus by viewModel.tribus.collectAsState()
    MisTribusScreen(tribus = tribus, onVerTribu = onVerTribu, onIrASeccion = onIrASeccion)
}

@Composable
fun MisTribusScreen(
    tribus: List<TribuConEvento>,
    onVerTribu: (Tribu) -> Unit,
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
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Titulo("Mis", "tribus")
                Text(
                    text = "Antes, durante y después de cada evento",
                    color = TextoSuave,
                    fontFamily = Nunito,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
            if (tribus.isEmpty()) {
                item {
                    SinTribus(onExplorar = { onIrASeccion(Seccion.EVENTOS) })
                }
            } else {
                item {
                    Text(
                        text = "PRÓXIMAS",
                        color = TextoGris,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        letterSpacing = 0.72.sp
                    )
                }
                items(tribus, key = { it.tribu.id }) { item ->
                    FilaMiTribu(item, onClick = { onVerTribu(item.tribu) })
                }
            }
        }
        BarraInferior(actual = Seccion.TRIBUS, misTribus = tribus.size, onIr = onIrASeccion)
    }
}

@Composable
fun FilaMiTribu(item: TribuConEvento, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Tarjeta)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(item.evento.foto),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.tribu.nombre,
                color = TextoClaro,
                fontFamily = Nunito,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${item.evento.titulo} · ${item.evento.fecha}",
                color = TextoSuave,
                fontFamily = Nunito,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Amarillo)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = "${item.tribu.integrantes.size} personas",
                color = Fondo,
                fontFamily = Nunito,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun SinTribus(onExplorar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Outlined.Groups, contentDescription = null, tint = IconoGris, modifier = Modifier.size(56.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Todavía no tienes tribus",
            color = TextoClaro,
            fontFamily = Nunito,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Elige un evento y únete a una tribu para ir acompañado.",
            color = TextoSuave,
            fontFamily = Nunito,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        BotonNaranja(texto = "Explorar eventos", onClick = onExplorar)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun MisTribusPreview() {
    MygosTheme {
        MisTribusScreen(
            tribus = listOf(TribuConEvento(tribusIniciales[1], eventosIniciales[0])),
            onVerTribu = {},
            onIrASeccion = {}
        )
    }
}
