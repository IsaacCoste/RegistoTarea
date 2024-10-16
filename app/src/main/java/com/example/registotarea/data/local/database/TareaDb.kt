package com.example.registotarea.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.registotarea.data.local.dao.ProyectoDao
import com.example.registotarea.data.local.dao.TareaDao
import com.example.registotarea.data.local.dao.UsuarioDao
import com.example.registotarea.data.local.entities.ProyectoEntity
import com.example.registotarea.data.local.entities.TareaEntity
import com.example.registotarea.data.local.entities.UsuarioEntity

@Database(
    entities = [
        UsuarioEntity::class,
        ProyectoEntity::class,
        TareaEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TareaDb : RoomDatabase() {
    abstract fun tareaDao(): TareaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun proyectoDao(): ProyectoDao
}