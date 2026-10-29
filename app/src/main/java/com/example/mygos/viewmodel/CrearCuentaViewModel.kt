package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.repository.CuentaRepository
import com.example.mygos.validacion.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Cada campo tiene su propio error para que la pantalla lo muestre debajo de ese campo
data class CrearCuentaEstado(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val errorNombre: String = "",
    val errorCorreo: String = "",
    val errorClave: String = "",
    val creada: Boolean = false
)

class CrearCuentaViewModel : ViewModel() {

    private val _estado = MutableStateFlow(CrearCuentaEstado())
    val estado: StateFlow<CrearCuentaEstado> = _estado.asStateFlow()

    // Si el campo ya mostraba un error se vuelve a validar al escribir, asi el mensaje se va apenas se corrige
    fun cambiarNombre(nombre: String) {
        _estado.update {
            it.copy(nombre = nombre, errorNombre = if (it.errorNombre.isNotEmpty()) Validaciones.nombre(nombre) else "")
        }
    }

    fun cambiarCorreo(correo: String) {
        _estado.update {
            it.copy(correo = correo, errorCorreo = if (it.errorCorreo.isNotEmpty()) Validaciones.correo(correo) else "")
        }
    }

    fun cambiarClave(clave: String) {
        _estado.update {
            it.copy(clave = clave, errorClave = if (it.errorClave.isNotEmpty()) Validaciones.clave(clave) else "")
        }
    }

    // Valida todos los campos a la vez. Si todo sirve guarda la cuenta y deja creada = true para que la pantalla navegue
    fun crearCuenta() {
        val actual = _estado.value
        val errorNombre = Validaciones.nombre(actual.nombre)
        val errorCorreo = Validaciones.correo(actual.correo)
        val errorClave = Validaciones.clave(actual.clave)
        _estado.update { it.copy(errorNombre = errorNombre, errorCorreo = errorCorreo, errorClave = errorClave) }
        if (errorNombre.isNotEmpty() || errorCorreo.isNotEmpty() || errorClave.isNotEmpty()) return

        viewModelScope.launch {
            val correo = actual.correo.trim()
            if (CuentaRepository.existeCorreo(correo)) {
                _estado.update { it.copy(errorCorreo = "Ya existe una cuenta con ese correo") }
            } else {
                CuentaRepository.crearCuenta(actual.nombre.trim(), correo, actual.clave)
                _estado.update { it.copy(creada = true) }
            }
        }
    }
}
