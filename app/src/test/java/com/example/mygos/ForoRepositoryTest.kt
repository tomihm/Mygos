package com.example.mygos

import com.example.mygos.remote.Comentario
import com.example.mygos.remote.ForoApi
import com.example.mygos.remote.PublicacionForo
import com.example.mygos.repository.ForoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

// API falsa: responde al instante sin internet y guarda lo que le llega para revisarlo en la prueba
class ApiFalsa : ForoApi {
    var recibida: PublicacionForo? = null

    override suspend fun publicaciones() = (1..3).map { PublicacionForo(it, "Titulo $it", "Cuerpo $it", "Vale_M") }
    override suspend fun publicacion(id: Int) = PublicacionForo(id, "Titulo", "Cuerpo", "Vale_M")
    override suspend fun publicar(publicacion: PublicacionForo): PublicacionForo {
        recibida = publicacion
        return publicacion.copy(id = 4)
    }
    override suspend fun editar(id: Int, publicacion: PublicacionForo) = publicacion
    override suspend fun borrar(id: Int) {}
    override suspend fun comentarios() = listOf(
        Comentario(1, 1, "Nico", "Yo voy"),
        Comentario(2, 2, "Cami", "Buena idea"),
        Comentario(3, 1, "Cami", "Nos vemos")
    )
    override suspend fun comentar(comentario: Comentario) = comentario.copy(id = 4)
}

class ForoRepositoryTest {

    @Test
    fun lasPublicacionesMasNuevasVanPrimero() = runBlocking {
        val lista = ForoRepository(ApiFalsa()).publicaciones()
        assertEquals(listOf(3, 2, 1), lista.map { it.id })
    }

    @Test
    fun publicarEnviaElTextoSinEspacios() = runBlocking {
        val api = ApiFalsa()
        ForoRepository(api).publicar("  Hola  ", " Nos vemos el sábado ", "Vale_M")
        assertEquals("Hola", api.recibida?.titulo)
        assertEquals("Nos vemos el sábado", api.recibida?.cuerpo)
        assertEquals(null, api.recibida?.id)
    }

    @Test
    fun soloTraeLosComentariosDeLaPublicacion() = runBlocking {
        val comentarios = ForoRepository(ApiFalsa()).comentarios(1)
        assertEquals(listOf("Yo voy", "Nos vemos"), comentarios.map { it.texto })
    }
}
