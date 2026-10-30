package com.example.mygos.repository

import com.example.mygos.Publicacion
import com.example.mygos.Usuario
import com.example.mygos.data.publicacionesIniciales
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

// Las fotos del muro viven en memoria, igual que las tribus. Las nuevas quedan primero
object MuroRepository {

    private val publicaciones = MutableStateFlow(publicacionesIniciales)

    fun deTribu(tribuId: Int): Flow<List<Publicacion>> {
        return publicaciones.map { lista -> lista.filter { it.tribuId == tribuId }.sortedByDescending { it.id } }
    }

    fun publicar(tribuId: Int, autor: Usuario, foto: String) {
        publicaciones.update { lista ->
            val id = (lista.maxOfOrNull { it.id } ?: 0) + 1
            lista + Publicacion(id, tribuId, autor, foto)
        }
    }
}
