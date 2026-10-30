package com.example.mygos.repository

import com.example.mygos.Tribu
import com.example.mygos.Usuario
import com.example.mygos.data.tribusIniciales
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

// Las tribus viven en memoria mientras la app esta abierta
object TribuRepository {

    private val _tribus = MutableStateFlow(tribusIniciales)
    val tribus: StateFlow<List<Tribu>> = _tribus.asStateFlow()

    fun tribusDe(eventoId: Int): Flow<List<Tribu>> {
        return tribus.map { lista -> lista.filter { it.eventoId == eventoId } }
    }

    fun tribu(id: Int): Flow<Tribu?> {
        return tribus.map { lista -> lista.find { it.id == id } }
    }

    // Las tribus donde el usuario es integrante, para "Mis tribus"
    fun misTribus(usuarioId: Int): Flow<List<Tribu>> {
        return tribus.map { lista -> lista.filter { tribu -> tribu.integrantes.any { it.id == usuarioId } } }
    }

    // Cambia la tribu en la lista por una copia que incluye al usuario.
    // Devuelve false si la tribu ya no tiene cupos o si el usuario ya esta en otra tribu del mismo evento
    fun unirse(tribu: Tribu, usuario: Usuario): Boolean {
        val actual = _tribus.value.find { it.id == tribu.id } ?: return false
        if (actual.integrantes.any { it.id == usuario.id }) return true
        if (actual.integrantes.size >= actual.cupos) return false
        if (_tribus.value.any { otra -> otra.eventoId == actual.eventoId && otra.integrantes.any { it.id == usuario.id } }) return false
        _tribus.update { lista ->
            lista.map { if (it.id == tribu.id) it.copy(integrantes = it.integrantes + usuario) else it }
        }
        return true
    }

    // Saca al usuario de la tribu y su cupo queda libre para otra persona
    fun salir(tribu: Tribu, usuario: Usuario) {
        _tribus.update { lista ->
            lista.map { actual ->
                if (actual.id == tribu.id) actual.copy(integrantes = actual.integrantes.filter { it.id != usuario.id }) else actual
            }
        }
    }
}
