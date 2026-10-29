package com.example.mygos.data

import androidx.room.Database
import androidx.room.RoomDatabase

// Solo las cuentas se guardan en la base, para que sigan ahi al cerrar la app
@Database(entities = [CuentaEntity::class], version = 1, exportSchema = false)
abstract class MygosDatabase : RoomDatabase() {
    abstract fun cuentaDao(): CuentaDao
}
