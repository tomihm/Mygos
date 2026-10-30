package com.example.mygos.repository

import com.example.mygos.Mensaje
import com.example.mygos.Usuario
import com.example.mygos.data.mensajesIniciales
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

// Mensajes del chat de cada tribu, en memoria y en el orden en que se enviaron
object ChatRepository {

    private val mensajes = MutableStateFlow(mensajesIniciales)

    fun deTribu(tribuId: Int): Flow<List<Mensaje>> {
        return mensajes.map { lista -> lista.filter { it.tribuId == tribuId } }
    }

    fun enviar(tribuId: Int, autor: Usuario, texto: String) {
        mensajes.update { lista ->
            val id = (lista.maxOfOrNull { it.id } ?: 0) + 1
            lista + Mensaje(id, tribuId, autor, texto.trim())
        }
    }
}
