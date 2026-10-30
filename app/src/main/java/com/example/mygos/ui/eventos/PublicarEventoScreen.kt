package com.example.mygos.ui.eventos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygos.Usuario
import com.example.mygos.repository.EventoRepository
import com.example.mygos.ui.componentes.BotonNaranja
import com.example.mygos.ui.componentes.Cabecera
import com.example.mygos.ui.componentes.CampoSelector
import com.example.mygos.ui.componentes.CampoTexto
import com.example.mygos.ui.componentes.Etiqueta
import com.example.mygos.ui.componentes.Filtros
import com.example.mygos.ui.theme.Fondo
import com.example.mygos.ui.theme.MygosTheme
import com.example.mygos.ui.theme.Naranja
import com.example.mygos.ui.theme.Nunito
import com.example.mygos.ui.theme.TextoSuave
import com.example.mygos.validacion.Validaciones
import com.example.mygos.viewmodel.PublicarEventoEstado
import com.example.mygos.viewmodel.PublicarEventoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun PublicarEventoRuta(
    usuario: Usuario,
    onVolver: () -> Unit,
    onPublicado: () -> Unit,
    viewModel: PublicarEventoViewModel = viewModel { PublicarEventoViewModel(usuario) }
) {
    val estado by viewModel.estado.collectAsState()
    LaunchedEffect(estado.publicado) {
        if (estado.publicado) onPublicado()
    }
    PublicarEventoScreen(
        estado = estado,
        onVolver = onVolver,
        onTitulo = viewModel::cambiarTitulo,
        onCategoria = viewModel::elegirCategoria,
        onFecha = viewModel::elegirFecha,
        onHora = viewModel::elegirHora,
        onLugar = viewModel::cambiarLugar,
        onDescripcion = viewModel::cambiarDescripcion,
        onPublicar = viewModel::publicar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicarEventoScreen(
    estado: PublicarEventoEstado,
    onVolver: () -> Unit,
    onTitulo: (String) -> Unit,
    onCategoria: (String) -> Unit,
    onFecha: (Long?) -> Unit,
    onHora: (Int, Int) -> Unit,
    onLugar: (String) -> Unit,
    onDescripcion: (String) -> Unit,
    onPublicar: () -> Unit
) {
    // Solo la pantalla decide si el selector esta abierto; el valor elegido se guarda en el ViewModel
    var verFecha by remember { mutableStateOf(false) }
    var verHora by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .navigationBarsPadding()
            .imePadding()
    ) {
        Cabecera(titulo = "Publicar evento", subtitulo = "Aparecerá en Explorar para que se armen tribus", onVolver = onVolver)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            CampoTexto(
                valor = estado.titulo,
                onCambio = onTitulo,
                etiqueta = "Nombre del evento",
                icono = Icons.Filled.Celebration,
                error = estado.errorTitulo,
                ayuda = "Ej: Noche de vinilos"
            )

            Spacer(modifier = Modifier.height(8.dp))
            Etiqueta("Categoría")
            Spacer(modifier = Modifier.height(8.dp))
            Filtros(
                categorias = EventoRepository.categorias.drop(1),
                seleccionada = estado.categoria,
                onSeleccionar = onCategoria,
                margen = 0.dp
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CampoSelector(
                    valor = estado.fecha?.let { textoFecha(it) } ?: "",
                    etiqueta = "Fecha",
                    icono = Icons.Filled.CalendarMonth,
                    error = estado.errorFecha,
                    onClick = { verFecha = true },
                    modifier = Modifier.weight(1f)
                )
                CampoSelector(
                    valor = estado.hora,
                    etiqueta = "Hora",
                    icono = Icons.Filled.Schedule,
                    error = estado.errorHora,
                    onClick = { verHora = true },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            CampoTexto(
                valor = estado.lugar,
                onCambio = onLugar,
                etiqueta = "Lugar",
                icono = Icons.Filled.Place,
                error = estado.errorLugar
            )
            CampoTexto(
                valor = estado.descripcion,
                onCambio = onDescripcion,
                etiqueta = "Descripción",
                icono = Icons.Filled.Notes,
                error = estado.errorDescripcion,
                ayuda = "${estado.descripcion.length} / ${Validaciones.MAX_DESCRIPCION}",
                lineas = 4
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        BotonNaranja(
            texto = "Publicar",
            onClick = onPublicar,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 12.dp)
        )
    }

    if (verFecha) {
        val estadoFecha = rememberDatePickerState(initialSelectedDateMillis = estado.fecha)
        DatePickerDialog(
            onDismissRequest = { verFecha = false },
            confirmButton = {
                TextButton(onClick = {
                    onFecha(estadoFecha.selectedDateMillis)
                    verFecha = false
                }) { Text("Listo", color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold) }
            },
            dismissButton = {
                TextButton(onClick = { verFecha = false }) {
                    Text("Cancelar", color = TextoSuave, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
                }
            }
        ) {
            DatePicker(state = estadoFecha)
        }
    }

    if (verHora) {
        val estadoHora = rememberTimePickerState(is24Hour = true)
        AlertDialog(
            onDismissRequest = { verHora = false },
            confirmButton = {
                TextButton(onClick = {
                    onHora(estadoHora.hour, estadoHora.minute)
                    verHora = false
                }) { Text("Listo", color = Naranja, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold) }
            },
            dismissButton = {
                TextButton(onClick = { verHora = false }) {
                    Text("Cancelar", color = TextoSuave, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold)
                }
            },
            text = { TimePicker(state = estadoHora) }
        )
    }
}

// El DatePicker entrega la fecha en UTC, por eso se muestra en UTC: si no, en Chile saldria el dia anterior
private fun textoFecha(fechaUtc: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("es-CL"))
    formato.timeZone = TimeZone.getTimeZone("UTC")
    return formato.format(Date(fechaUtc))
}


@Preview
@Composable
fun PublicarEventoPreview() {
    MygosTheme {
        PublicarEventoScreen(
            estado = PublicarEventoEstado(errorTitulo = "Escribe el nombre del evento", errorFecha = "Elige la fecha"),
            onVolver = {},
            onTitulo = {},
            onCategoria = {},
            onFecha = {},
            onHora = { _, _ -> },
            onLugar = {},
            onDescripcion = {},
            onPublicar = {}
        )
    }
}
