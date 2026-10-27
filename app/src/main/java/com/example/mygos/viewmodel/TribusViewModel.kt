package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mygos.Evento
import com.example.mygos.Tribu
import com.example.mygos.Usuario
import com.example.mygos.repository.TribuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TribusEstado(
    val tribus: List<Tribu>,
    val indice: Int = 0
) {
    val tribuActual: Tribu? get() = tribus.getOrNull(indice)
}

class TribusViewModel(private val evento: Evento) : ViewModel() {

    private val _estado = MutableStateFlow(TribusEstado(TribuRepository.tribusDe(evento)))
    val estado: StateFlow<TribusEstado> = _estado.asStateFlow()

    fun saltar() {
        _estado.update { it.copy(indice = it.indice + 1) }
    }

    fun verDeNuevo() {
        _estado.update { TribusEstado(TribuRepository.tribusDe(evento)) }
    }

    // Devuelve false si la tribu ya se lleno
    fun unirse(usuario: Usuario): Boolean {
        val tribu = _estado.value.tribuActual ?: return false
        val unido = TribuRepository.unirse(tribu, usuario)
        _estado.update { it.copy(tribus = TribuRepository.tribusDe(evento)) }
        return unido
    }
}
