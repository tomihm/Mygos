package com.example.mygos.ui.cuenta

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygos.ui.componentes.BotonNaranja
import com.example.mygos.ui.theme.Amarillo
import com.example.mygos.ui.theme.BotonOscuro
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.InstrumentSerif
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.ui.theme.Verde

@Composable
fun BienvenidaScreen(onIniciarSesion: () -> Unit, onCrearCuenta: () -> Unit) {
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

        Spacer(modifier = Modifier.height(28.dp))
        BotonNaranja(texto = "Iniciar sesión", onClick = onIniciarSesion)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = onCrearCuenta,
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BotonOscuro),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Crear cuenta",
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
fun BienvenidaPreview() {
    MygosTheme {
        BienvenidaScreen(onIniciarSesion = {}, onCrearCuenta = {})
    }
}
