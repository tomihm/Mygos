package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mygos.Usuario
import com.example.mygos.repository.EventoRepository
import com.example.mygos.validacion.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar
import java.util.TimeZone

data class PublicarEventoEstado(
    val titulo: String = "",
    val categoria: String = "Música",
    val fecha: Long? = null,
    val hora: String = "",
    val lugar: String = "",
    val descripcion: String = "",
    val errorTitulo: String = "",
    val errorFecha: String = "",
    val errorHora: String = "",
    val errorLugar: String = "",
    val errorDescripcion: String = "",
    val publicado: Boolean = false
)

class PublicarEventoViewModel(private val organizador: Usuario) : ViewModel() {

    private val _estado = MutableStateFlow(PublicarEventoEstado())
    val estado: StateFlow<PublicarEventoEstado> = _estado.asStateFlow()

    // Igual que en crear cuenta: si el campo ya tenia error se revalida al escribir
    fun cambiarTitulo(valor: String) {
        _estado.update { it.copy(titulo = valor, errorTitulo = if (it.errorTitulo.isNotEmpty()) Validaciones.titulo(valor) else "") }
    }

    fun elegirCategoria(valor: String) {
        _estado.update { it.copy(categoria = valor) }
    }

    fun elegirFecha(valor: Long?) {
        _estado.update { it.copy(fecha = valor, errorFecha = Validaciones.fecha(valor, hoyUtc())) }
    }

    fun elegirHora(hora: Int, minuto: Int) {
        val valor = "%02d:%02d".format(hora, minuto)
        _estado.update { it.copy(hora = valor, errorHora = "") }
    }

    fun cambiarLugar(valor: String) {
        _estado.update { it.copy(lugar = valor, errorLugar = if (it.errorLugar.isNotEmpty()) Validaciones.lugar(valor) else "") }
    }

    fun cambiarDescripcion(valor: String) {
        _estado.update {
            it.copy(descripcion = valor, errorDescripcion = if (it.errorDescripcion.isNotEmpty()) Validaciones.descripcion(valor) else "")
        }
    }

    fun publicar() {
        val actual = _estado.value
        val conErrores = actual.copy(
            errorTitulo = Validaciones.titulo(actual.titulo),
            errorFecha = Validaciones.fecha(actual.fecha, hoyUtc()),
            errorHora = Validaciones.hora(actual.hora),
            errorLugar = Validaciones.lugar(actual.lugar),
            errorDescripcion = Validaciones.descripcion(actual.descripcion)
        )
        val hayError = listOf(
            conErrores.errorTitulo, conErrores.errorFecha, conErrores.errorHora, conErrores.errorLugar, conErrores.errorDescripcion
        ).any { it.isNotEmpty() }
        if (hayError || actual.fecha == null) {
            _estado.value = conErrores
            return
        }
        EventoRepository.publicar(actual.titulo, actual.categoria, actual.fecha, actual.hora, actual.lugar, actual.descripcion, organizador)
        _estado.value = conErrores.copy(publicado = true)
    }

    // El DatePicker trabaja con la medianoche UTC; se arma la de hoy (segun el telefono) para poder comparar
    private fun hoyUtc(): Long {
        val local = Calendar.getInstance()
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.clear()
        utc.set(local.get(Calendar.YEAR), local.get(Calendar.MONTH), local.get(Calendar.DAY_OF_MONTH))
        return utc.timeInMillis
    }
}
