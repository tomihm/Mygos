package com.example.mygos.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

// Filas de las tablas de Xano. id es null al crear: Gson no lo envia y Xano lo asigna
data class PublicacionForo(
    val id: Int? = null,
    val titulo: String,
    val cuerpo: String,
    val autor: String
)

data class Comentario(
    val id: Int? = null,
    @SerializedName("publicacion_id") val publicacionId: Int,
    val autor: String,
    val texto: String
)

// Los 7 endpoints del foro, generados por Xano a partir de las tablas publicacion y comentario
interface ForoApi {

    @GET("publicacion")
    suspend fun publicaciones(): List<PublicacionForo>

    @GET("publicacion/{id}")
    suspend fun publicacion(@Path("id") id: Int): PublicacionForo

    @POST("publicacion")
    suspend fun publicar(@Body publicacion: PublicacionForo): PublicacionForo

    @PATCH("publicacion/{id}")
    suspend fun editar(@Path("id") id: Int, @Body publicacion: PublicacionForo): PublicacionForo

    @DELETE("publicacion/{id}")
    suspend fun borrar(@Path("id") id: Int)

    @GET("comentario")
    suspend fun comentarios(): List<Comentario>

    @POST("comentario")
    suspend fun comentar(@Body comentario: Comentario): Comentario
}

// Un solo cliente para toda la app, apuntando al backend de Mygos en Xano
object ClienteApi {
    val foro: ForoApi = Retrofit.Builder()
        .baseUrl("https://x8ki-letl-twmt.n7.xano.io/api:Xp9Ijf0e/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ForoApi::class.java)
}
