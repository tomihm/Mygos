package com.example.mygos

import com.example.mygos.validacion.Validaciones
import org.junit.Assert.assertEquals
import org.junit.Test

class ValidacionesTest {

    @Test
    fun correoValidoNoTieneError() {
        assertEquals("", Validaciones.correo("vale@mygos.cl"))
    }

    @Test
    fun correoSinArrobaDaError() {
        assertEquals("El correo no es válido", Validaciones.correo("vale.mygos.cl"))
    }

    @Test
    fun claveCortaDaError() {
        assertEquals("Mínimo 4 caracteres", Validaciones.clave("123"))
    }

    @Test
    fun fechaPasadaDaError() {
        val hoy = 1_000_000L
        assertEquals("La fecha ya pasó", Validaciones.fecha(hoy - 1, hoy))
        assertEquals("", Validaciones.fecha(hoy, hoy))
    }

    @Test
    fun tituloDePublicacionVacioDaError() {
        assertEquals("Escribe un título", Validaciones.tituloPublicacion("   "))
    }
}
