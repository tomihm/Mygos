package com.example.mygos.repository

import com.example.mygos.remote.Autor
import com.example.mygos.remote.ClienteApi
import com.example.mygos.remote.Comentario
import com.example.mygos.remote.ForoApi
import com.example.mygos.remote.PublicacionForo

// Puente entre el ViewModel y la API: el ViewModel no sabe que existe Retrofit
class ForoRepository(private val api: ForoApi = ClienteApi.foro) {

    // Solo las primeras 20 para no llenar la pantalla con las 100 de la API
    suspend fun publicaciones(): List<PublicacionForo> = api.publicaciones().take(20)

    suspend fun publicacion(id: Int): PublicacionForo = api.publicacion(id)

    suspend fun comentarios(publicacionId: Int): List<Comentario> = api.comentarios(publicacionId)

    suspend fun autor(id: Int): Autor = api.autor(id)

    suspend fun publicar(titulo: String, cuerpo: String): PublicacionForo =
        api.publicar(PublicacionForo(titulo = titulo.trim(), cuerpo = cuerpo.trim()))

    suspend fun editar(publicacion: PublicacionForo): PublicacionForo = api.editar(publicacion.id, publicacion)

    suspend fun borrar(id: Int) = api.borrar(id)
}
