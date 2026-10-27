package com.example.mygos.repository

import androidx.compose.ui.graphics.toArgb
import com.example.mygos.Cuenta
import com.example.mygos.Usuario
import com.example.mygos.data.CuentaDao
import com.example.mygos.data.UsuarioEntity
import com.example.mygos.data.aModelo
import com.example.mygos.ui.theme.AzulGris
import com.example.mygos.ui.theme.Dorado
import com.example.mygos.ui.theme.Ladrillo
import com.example.mygos.ui.theme.NaranjaOscuro
import com.example.mygos.ui.theme.Turquesa
import com.example.mygos.ui.theme.VerdeBosque
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object CuentaRepository {

    // Lo entrega MygosApp al abrir la app
    private lateinit var dao: CuentaDao

    private val coloresNuevos = listOf(Turquesa, Ladrillo, Dorado, AzulGris, NaranjaOscuro, VerdeBosque)

    // El usuario que inicio sesion, null si nadie ha entrado
    var usuarioActual: Usuario? = null
        private set

    fun iniciar(dao: CuentaDao) {
        this.dao = dao
    }

    fun cuentasDePrueba(): Flow<List<Cuenta>> {
        return dao.cuentasDePrueba().map { lista -> lista.map { it.aModelo() } }
    }

    suspend fun iniciarSesion(correo: String, clave: String): Usuario? {
        val cuenta = dao.buscar(correo.trim().lowercase())
        usuarioActual = if (cuenta != null && cuenta.cuenta.clave == clave) cuenta.usuario.aModelo() else null
        return usuarioActual
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
        val nuevo = UsuarioEntity(nombre = nombre.trim(), iniciales = iniciales.uppercase(), color = color.toArgb())
        val id = dao.crear(nuevo, correo.trim().lowercase(), clave)
        val usuario = nuevo.copy(id = id).aModelo()
        usuarioActual = usuario
        return usuario
    }
}
