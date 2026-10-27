package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mygos.Cuenta
import com.example.mygos.Usuario
import com.example.mygos.repository.CuentaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class InicioSesionEstado(
    val correo: String = "",
    val clave: String = "",
    val error: String = ""
)

class InicioSesionViewModel : ViewModel() {

    private val _estado = MutableStateFlow(InicioSesionEstado())
    val estado: StateFlow<InicioSesionEstado> = _estado.asStateFlow()

    val cuentasDePrueba: List<Cuenta> = CuentaRepository.obtenerCuentas()

    fun cambiarCorreo(correo: String) {
        _estado.update { it.copy(correo = correo, error = "") }
    }

    fun cambiarClave(clave: String) {
        _estado.update { it.copy(clave = clave, error = "") }
    }

    fun elegirCuenta(cuenta: Cuenta) {
        _estado.update { it.copy(correo = cuenta.correo, clave = cuenta.clave, error = "") }
    }

    // Devuelve el usuario si los datos son correctos, si no deja el error en el estado
    fun iniciarSesion(): Usuario? {
        val actual = _estado.value
        if (actual.correo.isBlank() || actual.clave.isBlank()) {
            _estado.update { it.copy(error = "Completa tu correo y contraseña") }
            return null
        }
        val usuario = CuentaRepository.iniciarSesion(actual.correo, actual.clave)
        if (usuario == null) {
            _estado.update { it.copy(error = "Correo o contraseña incorrectos") }
        }
        return usuario
    }
}
