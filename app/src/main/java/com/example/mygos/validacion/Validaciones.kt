package com.example.mygos.validacion

// Reglas de los formularios en un solo lugar. Cada funcion devuelve el mensaje de error, o "" si el valor sirve.
// No conoce Compose ni los ViewModel: la pantalla nunca decide si un dato es valido
object Validaciones {

    private val formatoCorreo = Regex("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")

    fun nombre(valor: String): String = when {
        valor.isBlank() -> "Escribe tu nombre de usuario"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        valor.trim().length > 20 -> "Máximo 20 caracteres"
        valor.none { it.isLetterOrDigit() } -> "Usa al menos una letra o número"
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

    const val MAX_DESCRIPCION = 300

    fun titulo(valor: String): String = when {
        valor.isBlank() -> "Escribe el nombre del evento"
        valor.trim().length < 5 -> "Debe tener al menos 5 caracteres"
        valor.trim().length > 40 -> "Máximo 40 caracteres"
        else -> ""
    }

    // fecha y hoy son la medianoche UTC del dia, como las entrega el DatePicker. Se recibe hoy para no depender del reloj
    fun fecha(fecha: Long?, hoy: Long): String = when {
        fecha == null -> "Elige la fecha"
        fecha < hoy -> "La fecha ya pasó"
        else -> ""
    }

    fun hora(valor: String): String = if (valor.isEmpty()) "Elige la hora" else ""

    fun lugar(valor: String): String = when {
        valor.isBlank() -> "Ingresa el lugar del evento"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        else -> ""
    }

    fun descripcion(valor: String): String = when {
        valor.isBlank() -> "Cuenta de qué se trata"
        valor.trim().length < 20 -> "Escribe al menos 20 caracteres"
        valor.length > MAX_DESCRIPCION -> "Máximo $MAX_DESCRIPCION caracteres"
        else -> ""
    }

    fun mensaje(valor: String): String = when {
        valor.isBlank() -> "Escribe un mensaje"
        valor.length > 300 -> "Máximo 300 caracteres"
        else -> ""
    }
}
