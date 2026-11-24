package com.example.mygos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val comentarios: List<Comentario> = emptyList(),
    val comentario: String = "",
    val errorComentario: String = ""
)

private const val SIN_CONEXION = "No se pudo conectar. Revisa tu internet"

class ForoViewModel(
    private val autor: String,
    private val repositorio: ForoRepository = ForoRepository()
) : ViewModel() {

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

    // Publica una nueva (POST) o guarda la que se esta editando (PATCH)
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
                    val nueva = repositorio.publicar(actual.titulo, actual.cuerpo, autor)
                    _estado.update { it.copy(publicaciones = listOf(nueva) + it.publicaciones) }
                } else {
                    val original = actual.publicaciones.first { it.id == editandoId }
                    val cambiada = repositorio.editar(original, actual.titulo, actual.cuerpo)
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
        val id = publicacion.id ?: return
        viewModelScope.launch {
            try {
                repositorio.borrar(id)
                _estado.update { estado -> estado.copy(publicaciones = estado.publicaciones.filter { it.id != id }) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = SIN_CONEXION) }
            }
        }
    }

    // Trae la publicacion recien leida de Xano y sus comentarios para el detalle
    fun abrir(publicacion: PublicacionForo) {
        val id = publicacion.id ?: return
        _estado.update { it.copy(abierta = publicacion, comentarios = emptyList(), comentario = "", errorComentario = "") }
        viewModelScope.launch {
            try {
                val completa = repositorio.publicacion(id)
                val comentarios = repositorio.comentarios(id)
                _estado.update { it.copy(abierta = completa, comentarios = comentarios) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = SIN_CONEXION) }
            }
        }
    }

    fun cerrar() {
        _estado.update { it.copy(abierta = null, comentarios = emptyList()) }
    }

    fun cambiarComentario(texto: String) {
        _estado.update { it.copy(comentario = texto, errorComentario = if (it.errorComentario.isNotEmpty()) Validaciones.mensaje(texto) else "") }
    }

    fun comentar() {
        val id = _estado.value.abierta?.id ?: return
        val texto = _estado.value.comentario
        val error = Validaciones.mensaje(texto)
        if (error.isNotEmpty()) {
            _estado.update { it.copy(errorComentario = error) }
            return
        }
        viewModelScope.launch {
            try {
                val nuevo = repositorio.comentar(id, autor, texto)
                _estado.update { it.copy(comentarios = it.comentarios + nuevo, comentario = "") }
            } catch (e: Exception) {
                _estado.update { it.copy(errorComentario = SIN_CONEXION) }
            }
        }
    }
}
