package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.Cuenta
import com.example.mygos.repository.CuentaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class InicioSesionEstado(
    val correo: String = "",
    val clave: String = "",
    val error: String = "",
    val entro: Boolean = false
)

class InicioSesionViewModel : ViewModel() {

    private val _estado = MutableStateFlow(InicioSesionEstado())
    val estado: StateFlow<InicioSesionEstado> = _estado.asStateFlow()

    val cuentasDePrueba: StateFlow<List<Cuenta>> = CuentaRepository.cuentasDePrueba()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarCorreo(correo: String) {
        _estado.update { it.copy(correo = correo, error = "") }
    }

    fun cambiarClave(clave: String) {
        _estado.update { it.copy(clave = clave, error = "") }
    }

    fun elegirCuenta(cuenta: Cuenta) {
        _estado.update { it.copy(correo = cuenta.correo, clave = cuenta.clave, error = "") }
    }

    // Si los datos son correctos deja entro = true y la pantalla navega
    fun iniciarSesion() {
        val actual = _estado.value
        if (actual.correo.isBlank() || actual.clave.isBlank()) {
            _estado.update { it.copy(error = "Completa tu correo y contraseña") }
            return
        }
        viewModelScope.launch {
            val usuario = CuentaRepository.iniciarSesion(actual.correo, actual.clave)
            if (usuario == null) {
                _estado.update { it.copy(error = "Correo o contraseña incorrectos") }
            } else {
                _estado.update { it.copy(entro = true) }
            }
        }
    }
}
