package com.example.mygos

import com.example.mygos.remote.Autor
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

    override suspend fun publicaciones() = (1..30).map { PublicacionForo(it, 1, "Titulo $it", "Cuerpo $it") }
    override suspend fun publicacion(id: Int) = PublicacionForo(id, 1, "Titulo", "Cuerpo")
    override suspend fun comentarios(publicacionId: Int) = emptyList<Comentario>()
    override suspend fun autor(id: Int) = Autor(id, "Vale", "vale")
    override suspend fun publicar(publicacion: PublicacionForo): PublicacionForo {
        recibida = publicacion
        return publicacion.copy(id = 101)
    }
    override suspend fun editar(id: Int, publicacion: PublicacionForo) = publicacion
    override suspend fun borrar(id: Int) {}
}

class ForoRepositoryTest {

    @Test
    fun soloEntregaLasPrimeras20() = runBlocking {
        val lista = ForoRepository(ApiFalsa()).publicaciones()
        assertEquals(20, lista.size)
        assertEquals(1, lista.first().id)
    }

    @Test
    fun publicarEnviaElTextoSinEspacios() = runBlocking {
        val api = ApiFalsa()
        ForoRepository(api).publicar("  Hola  ", " Nos vemos el sabado ")
        assertEquals("Hola", api.recibida?.titulo)
        assertEquals("Nos vemos el sabado", api.recibida?.cuerpo)
    }
}
