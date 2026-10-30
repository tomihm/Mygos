package com.example.mygos.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.Evento
import com.example.mygos.Tribu
import com.example.mygos.repository.EventoRepository
import com.example.mygos.repository.TribuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Locale

// Distancia maxima, en metros, para poder marcar "Ya llegue"
const val RADIO_LLEGADA = 300f

data class EncuentroEstado(
    val tribu: Tribu? = null,
    val evento: Evento? = null,
    val latitud: Double? = null,
    val longitud: Double? = null,
    val buscando: Boolean = false,
    val sinPermiso: Boolean = false,
    val llegue: Boolean = false,
    val error: String = ""
) {
    // Metros en linea recta hasta el lugar del evento; null si todavia no hay ubicacion
    val distancia: Float?
        get() {
            if (evento == null || latitud == null || longitud == null) return null
            val resultado = FloatArray(1)
            Location.distanceBetween(latitud, longitud, evento.latitud, evento.longitud, resultado)
            return resultado[0]
        }
}

fun textoDistancia(metros: Float): String {
    return if (metros < 1000) "${metros.toInt()} m" else String.format(Locale.forLanguageTag("es-CL"), "%.1f km", metros / 1000)
}

class EncuentroViewModel(tribuId: Int) : ViewModel() {

    private val local = MutableStateFlow(EncuentroEstado())

    val estado: StateFlow<EncuentroEstado> = combine(
        local,
        TribuRepository.tribu(tribuId),
        EventoRepository.eventos()
    ) { actual, tribu, eventos ->
        actual.copy(tribu = tribu, evento = eventos.find { it.id == tribu?.eventoId })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EncuentroEstado())

    fun permisoConcedido() {
        local.update { it.copy(sinPermiso = false, buscando = true, error = "") }
    }

    fun permisoNegado() {
        local.update { it.copy(sinPermiso = true, buscando = false) }
    }

    fun ubicacionRecibida(latitud: Double, longitud: Double) {
        local.update { it.copy(latitud = latitud, longitud = longitud, buscando = false, error = "") }
    }

    fun ubicacionNoDisponible() {
        local.update { it.copy(buscando = false, error = "No pudimos obtener tu ubicación. Revisa que el GPS esté activo.") }
    }

    // Solo deja confirmar la llegada si el GPS dice que la persona esta cerca del lugar
    fun yaLlegue() {
        val distancia = estado.value.distancia
        when {
            distancia == null -> local.update { it.copy(error = "Activa tu ubicación para confirmar que llegaste") }
            distancia > RADIO_LLEGADA -> local.update { it.copy(error = "Todavía estás a ${textoDistancia(distancia)} del lugar") }
            else -> local.update { it.copy(llegue = true, error = "") }
        }
    }

    fun errorMostrado() {
        local.update { it.copy(error = "") }
    }
}
