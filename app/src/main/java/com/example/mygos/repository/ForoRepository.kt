package com.example.mygos.repository

import com.example.mygos.remote.ClienteApi
import com.example.mygos.remote.Comentario
import com.example.mygos.remote.ForoApi
import com.example.mygos.remote.PublicacionForo

// Puente entre el ViewModel y la API: el ViewModel no sabe que existe Retrofit
class ForoRepository(private val api: ForoApi = ClienteApi.foro) {

    // Las mas nuevas primero
    suspend fun publicaciones(): List<PublicacionForo> = api.publicaciones().sortedByDescending { it.id }

    suspend fun publicacion(id: Int): PublicacionForo = api.publicacion(id)

    suspend fun publicar(titulo: String, cuerpo: String, autor: String): PublicacionForo =
        api.publicar(PublicacionForo(titulo = titulo.trim(), cuerpo = cuerpo.trim(), autor = autor))

    suspend fun editar(publicacion: PublicacionForo, titulo: String, cuerpo: String): PublicacionForo =
        api.editar(publicacion.id ?: 0, publicacion.copy(titulo = titulo.trim(), cuerpo = cuerpo.trim()))

    suspend fun borrar(id: Int) = api.borrar(id)

    // Xano entrega todos los comentarios y aqui se dejan solo los de la publicacion. Si fueran muchos, convendria filtrarlos en Xano
    suspend fun comentarios(publicacionId: Int): List<Comentario> =
        api.comentarios().filter { it.publicacionId == publicacionId }

    suspend fun comentar(publicacionId: Int, autor: String, texto: String): Comentario =
        api.comentar(Comentario(publicacionId = publicacionId, autor = autor, texto = texto.trim()))
}
