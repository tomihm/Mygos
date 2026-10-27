package com.example.mygos.repository

import com.example.mygos.Cuenta
import com.example.mygos.Usuario
import com.example.mygos.camiR
import com.example.mygos.nicoT
import com.example.mygos.ui.theme.AzulGris
import com.example.mygos.ui.theme.Dorado
import com.example.mygos.ui.theme.Ladrillo
import com.example.mygos.ui.theme.NaranjaOscuro
import com.example.mygos.ui.theme.Turquesa
import com.example.mygos.ui.theme.VerdeBosque
import com.example.mygos.valeM

// No hay backend, las cuentas viven en memoria mientras la app esta abierta
object CuentaRepository {

    private val cuentas = mutableListOf(
        Cuenta("vale@mygos.cl", "1234", valeM),
        Cuenta("nico@mygos.cl", "1234", nicoT),
        Cuenta("cami@mygos.cl", "1234", camiR)
    )

    private val coloresNuevos = listOf(Turquesa, Ladrillo, Dorado, AzulGris, NaranjaOscuro, VerdeBosque)

    fun obtenerCuentas(): List<Cuenta> = cuentas.toList()

    fun buscarCuenta(correo: String, clave: String): Usuario? {
        val cuenta = cuentas.find { it.correo == correo.trim().lowercase() && it.clave == clave }
        return cuenta?.usuario
    }

    fun existeCorreo(correo: String): Boolean {
        return cuentas.any { it.correo == correo.trim().lowercase() }
    }

    fun crearCuenta(nombre: String, correo: String, clave: String): Usuario {
        val palabras = nombre.trim().split(" ", ".", "_").filter { it.isNotEmpty() }
        val iniciales = if (palabras.size >= 2) {
            "${palabras[0][0]}${palabras[1][0]}"
        } else {
            palabras[0].take(2)
        }
        val color = coloresNuevos[cuentas.size % coloresNuevos.size]
        val usuario = Usuario(nombre.trim(), iniciales.uppercase(), color)
        cuentas.add(Cuenta(correo.trim().lowercase(), clave, usuario))
        return usuario
    }
}
