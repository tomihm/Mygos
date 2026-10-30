package com.example.mygos.ui.cuenta

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.Cuenta
import com.example.mygos.data.cuentasIniciales
import com.example.mygos.ui.componentes.Avatar
import com.example.mygos.ui.componentes.BotonCircular
import com.example.mygos.ui.componentes.BotonNaranja
import com.example.mygos.ui.componentes.CampoTexto
import com.example.mygos.ui.componentes.Titulo
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoGris
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.viewmodel.CrearCuentaEstado
import com.example.mygos.viewmodel.CrearCuentaViewModel
import com.example.mygos.viewmodel.InicioSesionEstado
import com.example.mygos.viewmodel.InicioSesionViewModel

// Conecta la pantalla con su ViewModel
@Composable
fun InicioSesionRuta(
    onVolver: () -> Unit,
    onEntrar: () -> Unit,
    onIrACrearCuenta: () -> Unit,
    viewModel: InicioSesionViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()
    val cuentas by viewModel.cuentasDePrueba.collectAsState()

    // Cuando el ViewModel confirma la sesion, se navega una sola vez
    LaunchedEffect(estado.entro) {
        if (estado.entro) onEntrar()
    }

    InicioSesionScreen(
        estado = estado,
        cuentasDePrueba = cuentas,
        onCambiarCorreo = viewModel::cambiarCorreo,
        onCambiarClave = viewModel::cambiarClave,
        onElegirCuenta = viewModel::elegirCuenta,
        onEntrar = viewModel::iniciarSesion,
        onVolver = onVolver,
        onIrACrearCuenta = onIrACrearCuenta
    )
}

// Solo dibuja: recibe el estado y avisa lo que toca el usuario
@Composable
fun InicioSesionScreen(
    estado: InicioSesionEstado,
    cuentasDePrueba: List<Cuenta>,
    onCambiarCorreo: (String) -> Unit,
    onCambiarClave: (String) -> Unit,
    onElegirCuenta: (Cuenta) -> Unit,
    onEntrar: () -> Unit,
    onVolver: () -> Unit,
    onIrACrearCuenta: () -> Unit
) {

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
            onCambio = onCambiarCorreo,
            etiqueta = "Correo",
            icono = Icons.Filled.Email,
            error = estado.errorCorreo,
            tipo = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(4.dp))
        CampoTexto(
            valor = estado.clave,
            onCambio = onCambiarClave,
            etiqueta = "Contraseña",
            icono = Icons.Filled.Lock,
            error = estado.errorClave,
            tipo = KeyboardType.Password,
            esClave = true
        )
        // El mensaje entra deslizandose y se va con fundido en vez de aparecer de golpe
        AnimatedVisibility(
            visible = estado.error.isNotEmpty(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Text(
                text = estado.error,
                color = Naranja,
                fontFamily = Nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        BotonNaranja(
            texto = "Entrar",
            onClick = onEntrar
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
            for (cuenta in cuentasDePrueba) {
                FilaCuenta(
                    cuenta = cuenta,
                    seleccionada = cuenta.correo == estado.correo,
                    onClick = { onElegirCuenta(cuenta) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        TextoEnlace("¿No tienes cuenta? ", "Crea una", onClick = onIrACrearCuenta)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CrearCuentaRuta(
    onVolver: () -> Unit,
    onCuentaCreada: () -> Unit,
    onIrAIniciarSesion: () -> Unit,
    viewModel: CrearCuentaViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()

    LaunchedEffect(estado.creada) {
        if (estado.creada) onCuentaCreada()
    }

    CrearCuentaScreen(
        estado = estado,
        onCambiarNombre = viewModel::cambiarNombre,
        onCambiarCorreo = viewModel::cambiarCorreo,
        onCambiarClave = viewModel::cambiarClave,
        onCrearCuenta = viewModel::crearCuenta,
        onVolver = onVolver,
        onIrAIniciarSesion = onIrAIniciarSesion
    )
}

@Composable
fun CrearCuentaScreen(
    estado: CrearCuentaEstado,
    onCambiarNombre: (String) -> Unit,
    onCambiarCorreo: (String) -> Unit,
    onCambiarClave: (String) -> Unit,
    onCrearCuenta: () -> Unit,
    onVolver: () -> Unit,
    onIrAIniciarSesion: () -> Unit
) {

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
            onCambio = onCambiarNombre,
            etiqueta = "Nombre de usuario",
            icono = Icons.Filled.Person,
            error = estado.errorNombre
        )
        Spacer(modifier = Modifier.height(4.dp))
        CampoTexto(
            valor = estado.correo,
            onCambio = onCambiarCorreo,
            etiqueta = "Correo",
            icono = Icons.Filled.Email,
            error = estado.errorCorreo,
            tipo = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(4.dp))
        CampoTexto(
            valor = estado.clave,
            onCambio = onCambiarClave,
            etiqueta = "Contraseña",
            icono = Icons.Filled.Lock,
            error = estado.errorClave,
            ayuda = "Mínimo 4 caracteres",
            tipo = KeyboardType.Password,
            esClave = true
        )

        Spacer(modifier = Modifier.height(20.dp))
        BotonNaranja(
            texto = "Crear cuenta",
            onClick = onCrearCuenta
        )

        Spacer(modifier = Modifier.height(20.dp))
        TextoEnlace("¿Ya tienes cuenta? ", "Inicia sesión", onClick = onIrAIniciarSesion)
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun FilaCuenta(cuenta: Cuenta, seleccionada: Boolean, onClick: () -> Unit) {
    val forma = RoundedCornerShape(14.dp)
    // El color cambia en 250 ms en vez de saltar, asi se nota cual cuenta se eligio
    val fondo by animateColorAsState(
        targetValue = if (seleccionada) Naranja.copy(alpha = 0.16f) else Tarjeta,
        animationSpec = tween(250),
        label = "fondoCuenta"
    )
    val borde by animateColorAsState(
        targetValue = if (seleccionada) Naranja else Color.White.copy(alpha = 0.08f),
        animationSpec = tween(250),
        label = "bordeCuenta"
    )

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
        InicioSesionScreen(
            estado = InicioSesionEstado(correo = "vale@mygos.cl", clave = "1234"),
            cuentasDePrueba = cuentasIniciales,
            onCambiarCorreo = {},
            onCambiarClave = {},
            onElegirCuenta = {},
            onEntrar = {},
            onVolver = {},
            onIrACrearCuenta = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun CrearCuentaPreview() {
    MygosTheme {
        CrearCuentaScreen(
            estado = CrearCuentaEstado(),
            onCambiarNombre = {},
            onCambiarCorreo = {},
            onCambiarClave = {},
            onCrearCuenta = {},
            onVolver = {},
            onIrAIniciarSesion = {}
        )
    }
}
