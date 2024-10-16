package com.example.registotarea.data.remote.dto

import com.example.registotarea.data.local.entities.UsuarioEntity

class UsuarioDto (
    val usuarioId: Int,
    val nombre: String,
    val correoElectronico: String,
    val dirrecion: String,
    val telefono: String
)

fun UsuarioDto.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        usuarioId = usuarioId,
        nombre = nombre,
        correoElectronico = correoElectronico,
        dirrecion = dirrecion,
        telefono = telefono
    )
}