package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygos.remote.Autor
import com.example.mygos.remote.Comentario
import com.example.mygos.remote.PublicacionForo
import com.example.mygos.repository.ForoRepository
import com.example.mygos.validacion.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ForoEstado(
    val publicaciones: List<PublicacionForo> = emptyList(),
    val cargando: Boolean = true,
    val error: String = "",
    val titulo: String = "",
    val cuerpo: String = "",
    val errorTitulo: String = "",
    val errorCuerpo: String = "",
    val enviando: Boolean = false,
    // id de la publicacion que se esta editando; null cuando el formulario crea una nueva
    val editandoId: Int? = null,
    val abierta: PublicacionForo? = null,
    val autor: Autor? = null,
    val comentarios: List<Comentario> = emptyList()
)

// La API de prueba tiene publicaciones del 1 al 100. Las que se crean en el telefono no existen alla,
// por eso se editan y se abren sin llamarla
private const val ULTIMA_DE_LA_API = 100

private const val SIN_CONEXION = "No se pudo conectar. Revisa tu internet"

class ForoViewModel(private val repositorio: ForoRepository = ForoRepository()) : ViewModel() {

    private val _estado = MutableStateFlow(ForoEstado())
    val estado: StateFlow<ForoEstado> = _estado

    init {
        cargar()
    }

    fun cargar() {
        _estado.update { it.copy(cargando = true, error = "") }
        viewModelScope.launch {
            try {
                val lista = repositorio.publicaciones()
                _estado.update { it.copy(publicaciones = lista, cargando = false) }
            } catch (e: Exception) {
                _estado.update { it.copy(cargando = false, error = SIN_CONEXION) }
            }
        }
    }

    fun cambiarTitulo(texto: String) {
        _estado.update { it.copy(titulo = texto, errorTitulo = if (it.errorTitulo.isNotEmpty()) Validaciones.tituloPublicacion(texto) else "") }
    }

    fun cambiarCuerpo(texto: String) {
        _estado.update { it.copy(cuerpo = texto, errorCuerpo = if (it.errorCuerpo.isNotEmpty()) Validaciones.mensaje(texto) else "") }
    }

    // Publica una nueva (POST) o guarda la que se esta editando (PUT)
    fun guardar() {
        val actual = _estado.value
        val errorTitulo = Validaciones.tituloPublicacion(actual.titulo)
        val errorCuerpo = Validaciones.mensaje(actual.cuerpo)
        if (errorTitulo.isNotEmpty() || errorCuerpo.isNotEmpty()) {
            _estado.update { it.copy(errorTitulo = errorTitulo, errorCuerpo = errorCuerpo) }
            return
        }
        _estado.update { it.copy(enviando = true, error = "") }
        viewModelScope.launch {
            try {
                val editandoId = actual.editandoId
                if (editandoId == null) {
                    val nueva = repositorio.publicar(actual.titulo, actual.cuerpo)
                    // La API siempre responde id 101, asi que se le da uno que no choque con la lista
                    val id = (actual.publicaciones.maxOfOrNull { it.id } ?: 0) + 1
                    _estado.update { it.copy(publicaciones = listOf(nueva.copy(id = id)) + it.publicaciones) }
                } else {
                    val original = actual.publicaciones.first { it.id == editandoId }
                    val cambiada = original.copy(titulo = actual.titulo.trim(), cuerpo = actual.cuerpo.trim())
                    if (editandoId <= ULTIMA_DE_LA_API) repositorio.editar(cambiada)
                    _estado.update { estado -> estado.copy(publicaciones = estado.publicaciones.map { if (it.id == editandoId) cambiada else it }) }
                }
                _estado.update { it.copy(titulo = "", cuerpo = "", editandoId = null, enviando = false) }
            } catch (e: Exception) {
                _estado.update { it.copy(enviando = false, error = SIN_CONEXION) }
            }
        }
    }

    fun empezarEdicion(publicacion: PublicacionForo) {
        _estado.update {
            it.copy(editandoId = publicacion.id, titulo = publicacion.titulo, cuerpo = publicacion.cuerpo, errorTitulo = "", errorCuerpo = "")
        }
    }

    fun cancelarEdicion() {
        _estado.update { it.copy(editandoId = null, titulo = "", cuerpo = "", errorTitulo = "", errorCuerpo = "") }
    }

    fun borrar(publicacion: PublicacionForo) {
        viewModelScope.launch {
            try {
                if (publicacion.id <= ULTIMA_DE_LA_API) repositorio.borrar(publicacion.id)
                _estado.update { estado -> estado.copy(publicaciones = estado.publicaciones.filter { it.id != publicacion.id }) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = SIN_CONEXION) }
            }
        }
    }

    // Trae la publicacion, su autor y sus comentarios para el detalle
    fun abrir(publicacion: PublicacionForo) {
        _estado.update { it.copy(abierta = publicacion, autor = null, comentarios = emptyList()) }
        if (publicacion.id > ULTIMA_DE_LA_API) return
        viewModelScope.launch {
            try {
                val completa = repositorio.publicacion(publicacion.id)
                val autor = repositorio.autor(completa.autorId)
                val comentarios = repositorio.comentarios(completa.id)
                // abierta no se reemplaza para no perder lo que se edito en el telefono
                _estado.update { it.copy(autor = autor, comentarios = comentarios) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = SIN_CONEXION) }
            }
        }
    }

    fun cerrar() {
        _estado.update { it.copy(abierta = null, autor = null, comentarios = emptyList()) }
    }
}
