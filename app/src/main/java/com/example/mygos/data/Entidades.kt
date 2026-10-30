package com.example.mygos.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mygos.Cuenta
import com.example.mygos.Usuario

// @Entity es una tabla de la base de datos. Cada CuentaEntity es una fila
@Entity(tableName = "cuentas")
data class CuentaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val correo: String,
    val clave: String,
    val nombre: String,
    val iniciales: String,
    // Room no sabe guardar Color, asi que se guarda como numero
    val color: Int
)

// Convierten entre la fila de la base y el modelo que usan las pantallas
// Las cuentas de prueba recuperan su foto; las cuentas nuevas no tienen
fun CuentaEntity.aModelo(): Cuenta {
    val foto = cuentasIniciales.find { it.usuario.id == id }?.usuario?.foto
    return Cuenta(correo, clave, Usuario(id, nombre, iniciales, Color(color), foto))
}

fun Cuenta.aEntidad() = CuentaEntity(usuario.id, correo, clave, usuario.nombre, usuario.iniciales, usuario.color.toArgb())
