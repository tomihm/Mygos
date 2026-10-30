package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.Evento
import com.example.mygos.Tribu
import com.example.mygos.Usuario
import com.example.mygos.repository.EventoRepository
import com.example.mygos.repository.TribuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class TribuDetalleEstado(
    val tribu: Tribu? = null,
    val evento: Evento? = null,
    val soyIntegrante: Boolean = false,
    val unido: Boolean = false,
    val confirmarSalida: Boolean = false,
    val error: String = ""
) {
    val llena: Boolean get() = tribu != null && tribu.integrantes.size >= tribu.cupos
}

// Lo usan el detalle de la tribu y la pantalla "¡Ya tienes tribu!"
class TribuDetalleViewModel(tribuId: Int, private val usuario: Usuario) : ViewModel() {

    private val local = MutableStateFlow(TribuDetalleEstado())

    val estado: StateFlow<TribuDetalleEstado> = combine(
        local,
        TribuRepository.tribu(tribuId),
        EventoRepository.eventos()
    ) { actual, tribu, eventos ->
        actual.copy(
            tribu = tribu,
            evento = eventos.find { it.id == tribu?.eventoId },
            soyIntegrante = tribu?.integrantes?.any { it.id == usuario.id } == true
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TribuDetalleEstado())

    fun unirse() {
        val tribu = estado.value.tribu ?: return
        if (TribuRepository.unirse(tribu, usuario)) {
            local.update { it.copy(unido = true) }
        } else {
            val mensaje = if (estado.value.evento?.yaTienesTribu == true) "Ya tienes tribu en este evento" else "Esta tribu ya está llena"
            local.update { it.copy(error = mensaje) }
        }
    }

    // Se llama despues de navegar, para que al volver atras no se navegue otra vez
    fun unionMostrada() {
        local.update { it.copy(unido = false) }
    }

    fun pedirSalir() {
        local.update { it.copy(confirmarSalida = true) }
    }

    fun cancelarSalir() {
        local.update { it.copy(confirmarSalida = false) }
    }

    fun salir() {
        val tribu = estado.value.tribu ?: return
        TribuRepository.salir(tribu, usuario)
        local.update { it.copy(confirmarSalida = false) }
    }

    fun errorMostrado() {
        local.update { it.copy(error = "") }
    }
}
