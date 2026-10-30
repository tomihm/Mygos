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

data class TribusEstado(
    val evento: Evento? = null,
    val tribus: List<Tribu> = emptyList(),
    val indice: Int = 0,
    val unidoA: Tribu? = null,
    val error: String = ""
) {
    val tribuActual: Tribu? get() = tribus.getOrNull(indice)
}

class TribusViewModel(eventoId: Int) : ViewModel() {

    // Lo que decide la pantalla: en que tribu va y el resultado de unirse
    private val local = MutableStateFlow(TribusEstado())

    val estado: StateFlow<TribusEstado> = combine(
        local,
        EventoRepository.evento(eventoId),
        TribuRepository.tribusDe(eventoId)
    ) { actual, evento, tribus ->
        actual.copy(evento = evento, tribus = tribus)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TribusEstado())

    fun saltar() {
        local.update { it.copy(indice = it.indice + 1) }
    }

    fun verDeNuevo() {
        local.update { it.copy(indice = 0) }
    }

    fun unirse(usuario: Usuario) {
        val tribu = estado.value.tribuActual ?: return
        if (TribuRepository.unirse(tribu, usuario)) {
            local.update { it.copy(unidoA = tribu) }
        } else {
            val mensaje = if (estado.value.evento?.yaTienesTribu == true) "Ya tienes tribu en este evento" else "Esta tribu ya está llena"
            local.update { it.copy(error = mensaje) }
        }
    }

    // Se llama despues de navegar, para que al volver atras no se navegue otra vez
    fun unionMostrada() {
        local.update { it.copy(unidoA = null) }
    }

    fun errorMostrado() {
        local.update { it.copy(error = "") }
    }
}
