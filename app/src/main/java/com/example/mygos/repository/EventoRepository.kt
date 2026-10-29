package com.example.mygos.repository

import com.example.mygos.Evento
import com.example.mygos.Tribu
import com.example.mygos.data.eventosIniciales
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Los eventos viven en memoria. Se arman junto a las tribus para saber cuantas tiene
// cada uno y si el usuario ya esta en alguna
object EventoRepository {

    private val categorias = listOf("Todos", "Música", "Cine", "Juegos")

    fun obtenerCategorias(): List<String> = categorias

    // Flow: cada vez que cambian las tribus, entrega la lista de nuevo
    fun eventos(): Flow<List<Evento>> {
        return TribuRepository.tribus.map { tribus -> eventosIniciales.map { completar(it, tribus) } }
    }

    fun evento(id: Int): Flow<Evento?> {
        return eventos().map { lista -> lista.find { it.id == id } }
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
