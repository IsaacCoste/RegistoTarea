package com.example.registotarea.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val usuarioId: Int? = null,
    val nombre: String,
    val correoElectronico: String,
    val dirrecion: String,
    val telefono: String
)
