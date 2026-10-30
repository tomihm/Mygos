package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.Evento
import com.example.mygos.Mensaje
import com.example.mygos.Tribu
import com.example.mygos.Usuario
import com.example.mygos.repository.ChatRepository
import com.example.mygos.repository.EventoRepository
import com.example.mygos.repository.TribuRepository
import com.example.mygos.validacion.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class ChatEstado(
    val tribu: Tribu? = null,
    val evento: Evento? = null,
    val mensajes: List<Mensaje> = emptyList(),
    val texto: String = "",
    val error: String = ""
)

class ChatViewModel(tribuId: Int, private val usuario: Usuario) : ViewModel() {

    // Lo que escribe el usuario; el resto llega de los repositorios
    private val local = MutableStateFlow(ChatEstado())

    val estado: StateFlow<ChatEstado> = combine(
        local,
        TribuRepository.tribu(tribuId),
        EventoRepository.eventos(),
        ChatRepository.deTribu(tribuId)
    ) { actual, tribu, eventos, mensajes ->
        actual.copy(tribu = tribu, evento = eventos.find { it.id == tribu?.eventoId }, mensajes = mensajes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChatEstado())

    fun cambiarTexto(texto: String) {
        local.update { it.copy(texto = texto, error = if (it.error.isNotEmpty()) Validaciones.mensaje(texto) else "") }
    }

    fun enviar() {
        val tribu = estado.value.tribu ?: return
        val texto = local.value.texto
        val error = Validaciones.mensaje(texto)
        if (error.isNotEmpty()) {
            local.update { it.copy(error = error) }
            return
        }
        ChatRepository.enviar(tribu.id, usuario, texto)
        local.update { it.copy(texto = "", error = "") }
    }
}
