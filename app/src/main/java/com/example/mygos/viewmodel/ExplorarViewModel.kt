package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.Evento
import com.example.mygos.repository.EventoRepository
import com.example.mygos.repository.TribuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ExplorarEstado(
    val categorias: List<String> = EventoRepository.categorias,
    val categoria: String = "Todos",
    val eventos: List<Evento> = emptyList(),
    val misTribus: Int = 0
)

class ExplorarViewModel(usuarioId: Int) : ViewModel() {

    private val categoria = MutableStateFlow("Todos")

    // combine: se recalcula cuando cambia la categoria, los eventos o las tribus del usuario
    val estado: StateFlow<ExplorarEstado> = combine(
        categoria,
        EventoRepository.eventos(),
        TribuRepository.misTribus(usuarioId)
    ) { elegida, eventos, misTribus ->
        ExplorarEstado(
            categoria = elegida,
            eventos = if (elegida == "Todos") eventos else eventos.filter { it.categoria == elegida },
            misTribus = misTribus.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExplorarEstado())

    fun elegirCategoria(nueva: String) {
        categoria.value = nueva
    }
}
