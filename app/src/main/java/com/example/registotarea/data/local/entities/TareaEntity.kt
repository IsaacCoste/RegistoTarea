package com.example.registotarea.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Tareas",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["usuarioId"],
            childColumns = ["usuarioId"]
        ),
        ForeignKey(
            entity = ProyectoEntity::class,
            parentColumns = ["proyectoId"],
            childColumns = ["proyectoId"]
        )
    ]
)
data class TareaEntity(
    @PrimaryKey
    val tareaId: Int? = null,
    val descripcion: String,
    val completada: Boolean,
    val usuarioId: Int,
    val proyectoId: Int
)
