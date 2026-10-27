package com.example.mygos.repository

import com.example.mygos.Tribu
import com.example.mygos.Usuario
import com.example.mygos.data.TribuDao
import com.example.mygos.data.TribuIntegranteRef
import com.example.mygos.data.aModelo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object TribuRepository {

    // Lo entrega MygosApp al abrir la app
    private lateinit var dao: TribuDao

    fun iniciar(dao: TribuDao) {
        this.dao = dao
    }

    fun tribusDe(eventoId: Int): Flow<List<Tribu>> {
        return dao.deEvento(eventoId).map { lista -> lista.map { it.aModelo() } }
    }

    // Devuelve false si la tribu ya no tiene cupos
    suspend fun unirse(tribu: Tribu, usuario: Usuario): Boolean {
        val actual = dao.porId(tribu.id) ?: return false
        if (actual.integrantes.any { it.id == usuario.id }) return true
        if (actual.integrantes.size >= actual.tribu.cupos) return false
        dao.insertarIntegrantes(listOf(TribuIntegranteRef(tribu.id, usuario.id)))
        return true
    }
}
