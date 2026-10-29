package com.example.mygos.validacion

// Reglas de los formularios en un solo lugar. Cada funcion devuelve el mensaje de error, o "" si el valor sirve.
// No conoce Compose ni los ViewModel: la pantalla nunca decide si un dato es valido
object Validaciones {

    private val formatoCorreo = Regex("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")

    fun nombre(valor: String): String = when {
        valor.isBlank() -> "Escribe tu nombre de usuario"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        valor.trim().length > 20 -> "Máximo 20 caracteres"
        else -> ""
    }

    fun correo(valor: String): String = when {
        valor.isBlank() -> "Escribe tu correo"
        !formatoCorreo.matches(valor.trim()) -> "El correo no es válido"
        else -> ""
    }

    fun clave(valor: String): String = when {
        valor.isEmpty() -> "Escribe tu contraseña"
        valor.length < 4 -> "Mínimo 4 caracteres"
        else -> ""
    }

    // Al iniciar sesion solo importa que no este vacia; el largo minimo se exige al crear la cuenta
    fun claveIngresada(valor: String): String = if (valor.isEmpty()) "Escribe tu contraseña" else ""
}
