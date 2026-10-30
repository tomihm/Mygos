package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.repository.CuentaRepository
import com.example.mygos.repository.EventoRepository
import com.example.mygos.repository.TribuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class PerfilEstado(
    val correo: String = "",
    val tribus: Int = 0,
    val eventos: Int = 0,
    val confirmarCierre: Boolean = false
)

class PerfilViewModel(usuarioId: Int) : ViewModel() {

    private val confirmarCierre = MutableStateFlow(false)

    // El correo se lee una vez de Room; las tribus y eventos se actualizan solos
    val estado: StateFlow<PerfilEstado> = combine(
        flow { emit(CuentaRepository.correoDe(usuarioId)) },
        TribuRepository.misTribus(usuarioId),
        EventoRepository.deOrganizador(usuarioId),
        confirmarCierre
    ) { correo, tribus, eventos, confirmar ->
        PerfilEstado(correo, tribus.size, eventos.size, confirmar)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PerfilEstado())

    fun pedirCerrarSesion() {
        confirmarCierre.update { true }
    }

    fun cancelarCierre() {
        confirmarCierre.update { false }
    }

    fun cerrarSesion() {
        confirmarCierre.update { false }
        CuentaRepository.cerrarSesion()
    }
}
