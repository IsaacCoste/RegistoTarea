package com.example.registotarea.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Proyectos")
data class ProyectoEntity(
    @PrimaryKey
    val proyectoId: Int? = null,
    val nombre: String,
    val descripcion: String,
    val presupuesto: Double
)
