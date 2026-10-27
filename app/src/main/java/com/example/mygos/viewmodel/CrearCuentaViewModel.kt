package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.repository.CuentaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CrearCuentaEstado(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val error: String = "",
    val creada: Boolean = false
)

class CrearCuentaViewModel : ViewModel() {

    private val _estado = MutableStateFlow(CrearCuentaEstado())
    val estado: StateFlow<CrearCuentaEstado> = _estado.asStateFlow()

    fun cambiarNombre(nombre: String) {
        _estado.update { it.copy(nombre = nombre, error = "") }
    }

    fun cambiarCorreo(correo: String) {
        _estado.update { it.copy(correo = correo, error = "") }
    }

    fun cambiarClave(clave: String) {
        _estado.update { it.copy(clave = clave, error = "") }
    }

    // Si todo es valido guarda la cuenta y deja creada = true para que la pantalla navegue
    fun crearCuenta() {
        val actual = _estado.value
        val error = when {
            actual.nombre.isBlank() || actual.correo.isBlank() || actual.clave.isBlank() -> "Completa todos los campos"
            !actual.correo.contains("@") || !actual.correo.contains(".") -> "El correo no es válido"
            actual.clave.length < 4 -> "La contraseña debe tener al menos 4 caracteres"
            else -> ""
        }
        if (error.isNotEmpty()) {
            _estado.update { it.copy(error = error) }
            return
        }
        viewModelScope.launch {
            if (CuentaRepository.existeCorreo(actual.correo)) {
                _estado.update { it.copy(error = "Ya existe una cuenta con ese correo") }
            } else {
                CuentaRepository.crearCuenta(actual.nombre, actual.correo, actual.clave)
                _estado.update { it.copy(creada = true) }
            }
        }
    }
}
