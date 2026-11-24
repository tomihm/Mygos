package com.example.mygos.repository

import android.content.SharedPreferences
import androidx.compose.ui.graphics.toArgb
import com.example.mygos.Usuario
import com.example.mygos.data.CuentaDao
import com.example.mygos.data.CuentaEntity
import com.example.mygos.data.aModelo
import com.example.mygos.ui.theme.AzulGris
import com.example.mygos.ui.theme.Dorado
import com.example.mygos.ui.theme.Ladrillo
import com.example.mygos.ui.theme.NaranjaOscuro
import com.example.mygos.ui.theme.Turquesa
import com.example.mygos.ui.theme.VerdeBosque

object CuentaRepository {

    // Lo entrega MygosApp al abrir la app
    private lateinit var dao: CuentaDao

    // Guarda el id del usuario con sesion abierta, para que siga dentro al volver a abrir la app
    private lateinit var sesion: SharedPreferences

    private val coloresNuevos = listOf(Turquesa, Ladrillo, Dorado, AzulGris, NaranjaOscuro, VerdeBosque)

    // El usuario que inicio sesion, null si nadie ha entrado
    var usuarioActual: Usuario? = null
        private set

    fun iniciar(dao: CuentaDao, sesion: SharedPreferences) {
        this.dao = dao
        this.sesion = sesion
    }

    // Si quedo una sesion abierta la ultima vez, vuelve a cargar a ese usuario desde Room
    suspend fun restaurarSesion() {
        val id = sesion.getInt("usuarioId", -1)
        if (id != -1) {
            usuarioActual = dao.buscarPorId(id)?.aModelo()?.usuario
        }
    }

    private fun guardarSesion(usuario: Usuario?) {
        usuarioActual = usuario
        if (usuario != null) {
            sesion.edit().putInt("usuarioId", usuario.id).apply()
        } else {
            sesion.edit().remove("usuarioId").apply()
        }
    }

    suspend fun iniciarSesion(correo: String, clave: String): Usuario? {
        val cuenta = dao.buscar(correo.trim().lowercase())
        guardarSesion(if (cuenta != null && cuenta.clave == clave) cuenta.aModelo().usuario else null)
        return usuarioActual
    }

    // La cuenta sigue guardada en Room; solo se olvida quien esta usando la app
    fun cerrarSesion() {
        guardarSesion(null)
    }

    // El correo para mostrarlo en el perfil
    suspend fun correoDe(usuarioId: Int): String {
        return dao.buscarPorId(usuarioId)?.correo ?: ""
    }

    suspend fun existeCorreo(correo: String): Boolean {
        return dao.buscar(correo.trim().lowercase()) != null
    }

    suspend fun crearCuenta(nombre: String, correo: String, clave: String): Usuario {
        val palabras = nombre.trim().split(" ", ".", "_").filter { it.isNotEmpty() }
        val iniciales = if (palabras.size >= 2) {
            "${palabras[0][0]}${palabras[1][0]}"
        } else {
            palabras[0].take(2)
        }
        val color = coloresNuevos[dao.contarCuentas() % coloresNuevos.size]
        val nueva = CuentaEntity(
            correo = correo.trim().lowercase(),
            clave = clave,
            nombre = nombre.trim(),
            iniciales = iniciales.uppercase(),
            color = color.toArgb()
        )
        val id = dao.insertar(nueva).toInt()
        val usuario = nueva.copy(id = id).aModelo().usuario
        guardarSesion(usuario)
        return usuario
    }
}
