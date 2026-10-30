package com.example.mygos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// Un DAO dice que consultas se pueden hacer. Room escribe el codigo que las ejecuta
@Dao
interface CuentaDao {
    @Query("SELECT * FROM cuentas WHERE correo = :correo")
    suspend fun buscar(correo: String): CuentaEntity?

    @Query("SELECT * FROM cuentas WHERE id = :id")
    suspend fun buscarPorId(id: Int): CuentaEntity?

    @Query("SELECT COUNT(*) FROM cuentas")
    suspend fun contarCuentas(): Int

    // Devuelve el id que Room le dio a la fila nueva
    @Insert
    suspend fun insertar(cuenta: CuentaEntity): Long

    @Insert
    suspend fun insertarCuentas(cuentas: List<CuentaEntity>)
}
