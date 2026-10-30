package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.Evento
import com.example.mygos.repository.EventoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MisEventosViewModel(usuarioId: Int) : ViewModel() {

    val eventos: StateFlow<List<Evento>> = EventoRepository.deOrganizador(usuarioId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
