package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.Evento
import com.example.mygos.repository.EventoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class EventoViewModel(eventoId: Int) : ViewModel() {

    // null mientras Room todavia no responde
    val evento: StateFlow<Evento?> = EventoRepository.evento(eventoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
