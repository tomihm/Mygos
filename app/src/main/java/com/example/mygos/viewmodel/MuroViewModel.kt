package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.Publicacion
import com.example.mygos.Tribu
import com.example.mygos.Usuario
import com.example.mygos.repository.MuroRepository
import com.example.mygos.repository.TribuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

// insignias guarda "idPersona:insignia" de las que ya se dieron, para pintar el chip marcado
data class MuroEstado(
    val tribu: Tribu? = null,
    val publicaciones: List<Publicacion> = emptyList(),
    val insignias: Set<String> = emptySet()
)

class MuroViewModel(tribuId: Int, private val usuario: Usuario) : ViewModel() {

    private val local = MutableStateFlow(MuroEstado())

    val estado: StateFlow<MuroEstado> = combine(
        local,
        TribuRepository.tribu(tribuId),
        MuroRepository.deTribu(tribuId)
    ) { actual, tribu, publicaciones ->
        actual.copy(tribu = tribu, publicaciones = publicaciones)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MuroEstado())

    // Se llama cuando la camara termino de guardar la foto en el archivo
    fun publicarFoto(foto: String) {
        val tribu = estado.value.tribu ?: return
        MuroRepository.publicar(tribu.id, usuario, foto)
    }

    // Tocar una insignia la da y tocarla otra vez la quita
    fun alternarInsignia(personaId: Int, insignia: String) {
        val clave = "$personaId:$insignia"
        local.update { it.copy(insignias = if (clave in it.insignias) it.insignias - clave else it.insignias + clave) }
    }
}
