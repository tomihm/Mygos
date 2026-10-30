package com.example.mygos.ui.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.Usuario
import com.example.mygos.ui.componentes.Avatar
import com.example.mygos.ui.componentes.BarraInferior
import com.example.mygos.ui.componentes.BotonContorno
import com.example.mygos.ui.componentes.CajaDato
import com.example.mygos.ui.componentes.Seccion
import com.example.mygos.ui.componentes.Subtitulo
import com.example.mygos.ui.componentes.rayas
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.IconoGris
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.Tarjeta
import com.example.mygos.ui.theme.TextoClaro
import com.example.mygos.ui.theme.TextoDescripcion
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.ui.theme.Verde
import com.example.mygos.valeM
import com.example.mygos.viewmodel.PerfilEstado
import com.example.mygos.viewmodel.PerfilViewModel

@Composable
fun PerfilRuta(
    usuario: Usuario,
    onIrASeccion: (Seccion) -> Unit,
    onMisEventos: () -> Unit,
    onPublicar: () -> Unit,
    onSesionCerrada: () -> Unit,
    viewModel: PerfilViewModel = viewModel { PerfilViewModel(usuario.id) }
) {
    val estado by viewModel.estado.collectAsState()
    PerfilScreen(
        usuario = usuario,
        estado = estado,
        onIrASeccion = onIrASeccion,
        onMisEventos = onMisEventos,
        onPublicar = onPublicar,
        onPedirCerrar = viewModel::pedirCerrarSesion,
        onCancelarCerrar = viewModel::cancelarCierre,
        onCerrar = {
            viewModel.cerrarSesion()
            onSesionCerrada()
        }
    )
}

@Composable
fun PerfilScreen(
    usuario: Usuario,
    estado: PerfilEstado,
    onIrASeccion: (Seccion) -> Unit,
    onMisEventos: () -> Unit,
    onPublicar: () -> Unit,
    onPedirCerrar: () -> Unit,
    onCancelarCerrar: () -> Unit,
    onCerrar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Portada con el color del usuario, como la "foto" del diseño
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(usuario.color)
                    .rayas()
            ) {
                if (usuario.foto != null) {
                    Image(
                        painter = painterResource(usuario.foto),
                        contentDescription = "Foto de ${usuario.nombre}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Brush.verticalGradient(0.4f to Color.Transparent, 1f to Fondo))
                )
                if (usuario.foto == null) Avatar(
                    usuario = usuario,
                    tamano = 110.dp,
                    tamanoTexto = 38.sp,
                    borde = 3.dp,
                    colorBorde = TextoClaro,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 30.dp)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Verde.copy(alpha = 0.18f))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = Verde, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Identidad verificada", color = Verde, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(usuario.nombre, color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 32.sp)
                    Text(estado.correo, color = TextoSuave, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CajaDato("TRIBUS", "${estado.tribus}", Modifier.weight(1f))
                    CajaDato("EVENTOS PUBLICADOS", "${estado.eventos}", Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(22.dp))
                Subtitulo("Como organizador")
                Spacer(modifier = Modifier.height(10.dp))
                FilaOpcion(Icons.Filled.Event, "Mis eventos", onMisEventos)
                Spacer(modifier = Modifier.height(8.dp))
                FilaOpcion(Icons.Filled.Add, "Publicar un evento", onPublicar)
                Spacer(modifier = Modifier.height(22.dp))
                BotonContorno(texto = "Cerrar sesión", onClick = onPedirCerrar, icono = Icons.AutoMirrored.Filled.Logout)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        BarraInferior(actual = Seccion.PERFIL, misTribus = estado.tribus, onIr = onIrASeccion)
    }

    if (estado.confirmarCierre) {
        AlertDialog(
            onDismissRequest = onCancelarCerrar,
            title = { Text("¿Cerrar sesión?", fontFamily = Nunito, fontWeight = FontWeight.Black) },
            text = { Text("Tu cuenta queda guardada en el teléfono para volver a entrar.", fontFamily = Nunito) },
            confirmButton = {
                TextButton(onClick = onCerrar) {
                    Text("Cerrar sesión", color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(onClick = onCancelarCerrar) {
                    Text("Cancelar", color = TextoSuave, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
                }
            },
            containerColor = Tarjeta,
            titleContentColor = TextoClaro,
            textContentColor = TextoDescripcion
        )
    }
}

@Composable
fun FilaOpcion(icono: ImageVector, texto: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Tarjeta)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = Naranja, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(texto, color = TextoClaro, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = IconoGris)
    }
}

@Preview
@Composable
fun PerfilPreview() {
    MygosTheme {
        PerfilScreen(valeM, PerfilEstado("vale@mygos.cl", 1, 0), {}, {}, {}, {}, {}, {})
    }
}
