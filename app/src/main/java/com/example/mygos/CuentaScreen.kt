package com.example.mygos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.InstrumentSerif
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.viewmodel.CrearCuentaViewModel
import com.example.mygos.viewmodel.InicioSesionViewModel

@Composable
fun InicioSesionScreen(
    onVolver: () -> Unit,
    onEntrar: (Usuario) -> Unit,
    onIrACrearCuenta: () -> Unit,
    viewModel: InicioSesionViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        BotonCircular(Icons.AutoMirrored.Filled.ArrowBack, "Volver", onClick = onVolver)
        Spacer(modifier = Modifier.height(24.dp))
        Titulo("Inicia", "sesión")
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Entra con tu correo para ver tus eventos y tribus.",
            color = TextoSuave,
            fontFamily = Nunito,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(24.dp))
        CampoTexto(
            valor = estado.correo,
            onCambio = viewModel::cambiarCorreo,
            etiqueta = "Correo",
            tipo = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(12.dp))
        CampoTexto(
            valor = estado.clave,
            onCambio = viewModel::cambiarClave,
            etiqueta = "Contraseña",
            tipo = KeyboardType.Password,
            esClave = true
        )
        if (estado.error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = estado.error, color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))
        BotonNaranja(
            texto = "Entrar",
            onClick = {
                val usuario = viewModel.iniciarSesion()
                if (usuario != null) onEntrar(usuario)
            }
        )

        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "CUENTAS DE PRUEBA",
            color = TextoGris,
            fontFamily = Nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            letterSpacing = 0.72.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (cuenta in viewModel.cuentasDePrueba) {
                FilaCuenta(
                    cuenta = cuenta,
                    seleccionada = cuenta.correo == estado.correo,
                    onClick = { viewModel.elegirCuenta(cuenta) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        TextoEnlace("¿No tienes cuenta? ", "Crea una", onClick = onIrACrearCuenta)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CrearCuentaScreen(
    onVolver: () -> Unit,
    onCuentaCreada: (Usuario) -> Unit,
    onIrAIniciarSesion: () -> Unit,
    viewModel: CrearCuentaViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        BotonCircular(Icons.AutoMirrored.Filled.ArrowBack, "Volver", onClick = onVolver)
        Spacer(modifier = Modifier.height(24.dp))
        Titulo("Crea tu", "cuenta")
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Con la misma cuenta vas a eventos y también puedes organizarlos.",
            color = TextoSuave,
            fontFamily = Nunito,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(24.dp))
        CampoTexto(
            valor = estado.nombre,
            onCambio = viewModel::cambiarNombre,
            etiqueta = "Nombre de usuario"
        )
        Spacer(modifier = Modifier.height(12.dp))
        CampoTexto(
            valor = estado.correo,
            onCambio = viewModel::cambiarCorreo,
            etiqueta = "Correo",
            tipo = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(12.dp))
        CampoTexto(
            valor = estado.clave,
            onCambio = viewModel::cambiarClave,
            etiqueta = "Contraseña",
            tipo = KeyboardType.Password,
            esClave = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (estado.error.isNotEmpty()) {
            Text(text = estado.error, color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        } else {
            Text(text = "Mínimo 4 caracteres", color = TextoGris, fontFamily = Nunito, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))
        BotonNaranja(
            texto = "Crear cuenta",
            onClick = {
                val usuario = viewModel.crearCuenta()
                if (usuario != null) onCuentaCreada(usuario)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        TextoEnlace("¿Ya tienes cuenta? ", "Inicia sesión", onClick = onIrAIniciarSesion)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun Titulo(normal: String, cursiva: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 32.sp)) {
                append("$normal ")
            }
            withStyle(SpanStyle(fontFamily = InstrumentSerif, fontSize = 40.sp)) {
                append(cursiva)
            }
        },
        color = TextoClaro
    )
}

@Composable
fun FilaCuenta(cuenta: Cuenta, seleccionada: Boolean, onClick: () -> Unit) {
    val forma = RoundedCornerShape(14.dp)
    val fondo = if (seleccionada) Naranja.copy(alpha = 0.16f) else Tarjeta
    val borde = if (seleccionada) Naranja else Color.White.copy(alpha = 0.08f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(forma)
            .background(fondo)
            .border(1.dp, borde, forma)
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(usuario = cuenta.usuario, tamano = 32.dp, tamanoTexto = 12.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cuenta.usuario.nombre,
                color = TextoClaro,
                fontFamily = Nunito,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
            Text(
                text = cuenta.correo,
                color = TextoSuave,
                fontFamily = Nunito,
                fontSize = 12.sp
            )
        }
        Text(
            text = "clave: ${cuenta.clave}",
            color = TextoGris,
            fontFamily = Nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

@Composable
fun TextoEnlace(texto: String, enlace: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = texto, color = TextoSuave, fontFamily = Nunito, fontSize = 14.sp)
        Text(
            text = enlace,
            color = Naranja,
            fontFamily = Nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onClick() }
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun InicioSesionPreview() {
    MygosTheme {
        InicioSesionScreen(onVolver = {}, onEntrar = {}, onIrACrearCuenta = {})
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun CrearCuentaPreview() {
    MygosTheme {
        CrearCuentaScreen(onVolver = {}, onCuentaCreada = {}, onIrAIniciarSesion = {})
    }
}
