package com.example.mygos.repository

import com.example.mygos.Evento
import com.example.mygos.R
import com.example.mygos.Tribu
import com.example.mygos.Usuario
import com.example.mygos.data.eventosIniciales
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.Calendar
import java.util.TimeZone

// Los eventos viven en memoria. Se arman junto a las tribus para saber cuantas tiene
// cada uno y si el usuario ya esta en alguna
object EventoRepository {

    val categorias = listOf("Todos", "Música", "Cine", "Juegos")

    private val base = MutableStateFlow(eventosIniciales)

    private val meses = listOf("enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre")
    private val dias = listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")

    // Flow: cada vez que cambian los eventos o las tribus, entrega la lista de nuevo
    fun eventos(): Flow<List<Evento>> {
        return combine(base, TribuRepository.tribus) { eventos, tribus -> eventos.map { completar(it, tribus) } }
    }

    fun evento(id: Int): Flow<Evento?> {
        return eventos().map { lista -> lista.find { it.id == id } }
    }

    fun deOrganizador(usuarioId: Int): Flow<List<Evento>> {
        return eventos().map { lista -> lista.filter { it.organizador.id == usuarioId } }
    }

    // fechaUtc es lo que entrega el DatePicker: la medianoche del dia elegido en hora UTC
    fun publicar(
        titulo: String,
        categoria: String,
        fechaUtc: Long,
        hora: String,
        lugar: String,
        descripcion: String,
        organizador: Usuario
    ) {
        val dia = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = fechaUtc }
        val numero = dia.get(Calendar.DAY_OF_MONTH)
        val mes = meses[dia.get(Calendar.MONTH)]
        base.update { lista ->
            val nuevo = Evento(
                id = (lista.maxOfOrNull { it.id } ?: 0) + 1,
                titulo = titulo.trim(),
                organizador = organizador,
                hora = hora,
                mes = mes.take(3).uppercase(),
                dia = numero.toString().padStart(2, '0'),
                categoria = categoria,
                foto = fotoDe(categoria),
                tribus = 0,
                yaTienesTribu = false,
                // el organizador aparece como primer asistente
                asistentes = listOf(organizador),
                fecha = "${dias[dia.get(Calendar.DAY_OF_WEEK) - 1]} $numero de $mes",
                horario = hora,
                lugar = lugar.trim(),
                // shortcut: los eventos nuevos usan el centro de Santiago; pedir la ubicacion en el formulario si se necesita
                latitud = -33.4372,
                longitud = -70.6506,
                detalleLugar = "Publicado por ${organizador.nombre}",
                descripcion = descripcion.trim(),
                buscanGrupo = 0
            )
            lista + nuevo
        }
    }

    private fun fotoDe(categoria: String): Int = when (categoria) {
        "Cine" -> R.drawable.evento_cine
        "Juegos" -> R.drawable.evento_juegos
        else -> R.drawable.evento_vinilos
    }

    private fun completar(evento: Evento, tribus: List<Tribu>): Evento {
        val delEvento = tribus.filter { it.eventoId == evento.id }
        val usuarioId = CuentaRepository.usuarioActual?.id
        return evento.copy(
            tribus = delEvento.size,
            yaTienesTribu = delEvento.any { tribu -> tribu.integrantes.any { it.id == usuarioId } }
        )
    }
}
