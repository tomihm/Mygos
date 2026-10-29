package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.Evento
import com.example.mygos.Tribu
import com.example.mygos.repository.EventoRepository
import com.example.mygos.repository.TribuRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// Cada tribu va junto a su evento para mostrar el nombre, la fecha y la foto
data class TribuConEvento(val tribu: Tribu, val evento: Evento)

class MisTribusViewModel(usuarioId: Int) : ViewModel() {

    val tribus: StateFlow<List<TribuConEvento>> = combine(
        TribuRepository.misTribus(usuarioId),
        EventoRepository.eventos()
    ) { tribus, eventos ->
        tribus.mapNotNull { tribu ->
            eventos.find { it.id == tribu.eventoId }?.let { TribuConEvento(tribu, it) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
