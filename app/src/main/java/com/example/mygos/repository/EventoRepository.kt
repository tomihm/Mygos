package com.example.mygos.repository

import com.example.mygos.Evento
import com.example.mygos.data.EventoDao
import com.example.mygos.data.aModelo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object EventoRepository {

    // Lo entrega MygosApp al abrir la app
    private lateinit var dao: EventoDao

    private val categorias = listOf("Todos", "Música", "Cine", "Juegos")

    fun iniciar(dao: EventoDao) {
        this.dao = dao
    }

    fun obtenerCategorias(): List<String> = categorias

    // Flow: cada vez que cambian las tablas, entrega la lista de nuevo
    fun eventos(): Flow<List<Evento>> {
        return dao.todos().map { lista -> lista.map { it.aModelo(CuentaRepository.usuarioActual?.id) } }
    }

    fun evento(id: Int): Flow<Evento?> {
        return dao.porId(id).map { it?.aModelo(CuentaRepository.usuarioActual?.id) }
    }
}
