package com.example.mygos

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygos.ui.theme.Amarillo
import com.example.mygos.ui.theme.BotonOscuro
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.InstrumentSerif
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.ui.theme.Verde

@Composable
fun LoginScreen(onEntrar: (Usuario) -> Unit) {
    val context = LocalContext.current
    var seleccionado by remember { mutableStateOf(usuariosDePrueba[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "mygos",
            color = TextoClaro,
            fontFamily = Nunito,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            letterSpacing = (-0.44).sp
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Anillos()
        }

        Text(
            text = "Encuentra tu",
            color = TextoClaro,
            fontFamily = Nunito,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "tribu",
            color = TextoClaro,
            fontFamily = InstrumentSerif,
            fontSize = 66.sp,
            lineHeight = 62.7.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Elige un evento y te armamos un grupo con gente afín para ir juntos. Nadie llega solo.",
            color = TextoSuave,
            fontFamily = Nunito,
            fontSize = 15.sp,
            lineHeight = 21.75.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
        )

        Spacer(modifier = Modifier.height(22.dp))
        Text(
            text = "USUARIO DE PRUEBA",
            color = TextoGris,
            fontFamily = Nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            letterSpacing = 0.72.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (usuario in usuariosDePrueba) {
                BotonUsuario(
                    usuario = usuario,
                    seleccionado = usuario == seleccionado,
                    onClick = { seleccionado = usuario },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onEntrar(seleccionado) },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Naranja),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .shadow(14.dp, RoundedCornerShape(16.dp), spotColor = Naranja, ambientColor = Naranja)
        ) {
            Text(
                text = "Entrar como asistente",
                color = Color.White,
                fontFamily = Nunito,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                // La parte del organizador todavia no esta hecha
                Toast.makeText(context, "Próximamente", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BotonOscuro),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(
                text = "Entrar como organizador",
                color = TextoClaro,
                fontFamily = Nunito,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.VerifiedUser,
                contentDescription = null,
                tint = Verde,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Modo demo · perfiles verificados ficticios",
                color = TextoGris,
                fontFamily = Nunito,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun BotonUsuario(
    usuario: Usuario,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(14.dp)
    val fondo = if (seleccionado) Naranja.copy(alpha = 0.16f) else Tarjeta
    val borde = if (seleccionado) Naranja else Color.White.copy(alpha = 0.08f)

    Row(
        modifier = modifier
            .height(44.dp)
            .clip(forma)
            .background(fondo)
            .border(1.dp, borde, forma)
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(usuario = usuario, tamano = 28.dp, tamanoTexto = 11.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = usuario.nombre,
            color = TextoClaro,
            fontFamily = Nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp,
            maxLines = 1
        )
    }
}

@Composable
fun Anillos() {
    Canvas(
        modifier = Modifier
            .size(width = 310.dp, height = 264.dp)
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    ) {
        val grosor = 24.dp.toPx()
        val radio = 105.dp.toPx() - grosor / 2
        drawCircle(
            color = Amarillo,
            radius = radio,
            center = Offset(105.dp.toPx(), 105.dp.toPx()),
            style = Stroke(width = grosor)
        )
        drawCircle(
            color = Naranja,
            radius = radio,
            center = Offset(205.dp.toPx(), 159.dp.toPx()),
            style = Stroke(width = grosor),
            blendMode = BlendMode.Screen
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun LoginPreview() {
    MygosTheme {
        LoginScreen(onEntrar = {})
    }
}
