package com.example.mygos.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Lo que devuelve la API. SerializedName une el nombre en ingles del JSON con el nombre en español de la app
data class PublicacionForo(
    val id: Int = 0,
    @SerializedName("userId") val autorId: Int = 1,
    @SerializedName("title") val titulo: String,
    @SerializedName("body") val cuerpo: String
)

data class Comentario(
    val id: Int,
    @SerializedName("name") val asunto: String,
    @SerializedName("email") val correo: String,
    @SerializedName("body") val texto: String
)

data class Autor(
    val id: Int,
    @SerializedName("name") val nombre: String,
    @SerializedName("username") val usuario: String
)

// Los 7 endpoints que usa el foro de la comunidad
interface ForoApi {

    @GET("posts")
    suspend fun publicaciones(): List<PublicacionForo>

    @GET("posts/{id}")
    suspend fun publicacion(@Path("id") id: Int): PublicacionForo

    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") publicacionId: Int): List<Comentario>

    @GET("users/{id}")
    suspend fun autor(@Path("id") id: Int): Autor

    @POST("posts")
    suspend fun publicar(@Body publicacion: PublicacionForo): PublicacionForo

    @PUT("posts/{id}")
    suspend fun editar(@Path("id") id: Int, @Body publicacion: PublicacionForo): PublicacionForo

    @DELETE("posts/{id}")
    suspend fun borrar(@Path("id") id: Int)
}

// Un solo cliente para toda la app. JSONPlaceholder es una API publica de prueba: responde como si guardara, pero no guarda
object ClienteApi {
    val foro: ForoApi = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ForoApi::class.java)
}
