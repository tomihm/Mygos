package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mygos.Evento
import com.example.mygos.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ExplorarEstado(
    val categorias: List<String> = EventoRepository.obtenerCategorias(),
    val categoria: String = "Todos",
    val eventos: List<Evento> = EventoRepository.obtenerEventos()
)

class ExplorarViewModel : ViewModel() {

    private val _estado = MutableStateFlow(ExplorarEstado())
    val estado: StateFlow<ExplorarEstado> = _estado.asStateFlow()

    fun elegirCategoria(categoria: String) {
        _estado.update {
            it.copy(categoria = categoria, eventos = EventoRepository.eventosDeCategoria(categoria))
        }
    }
}
