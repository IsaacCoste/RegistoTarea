package com.example.registotarea.data.remote.dto

import com.example.registotarea.data.local.entities.TareaEntity

class TareaDto(
    val tareaId: Int,
    val descripcion: String,
    val completada: Boolean,
    val usuarioId: Int,
    val proyectoId: Int
)

fun TareaDto.toEntity(): TareaEntity {
    return TareaEntity(
        tareaId = tareaId,
        descripcion = descripcion,
        completada = completada,
        usuarioId = usuarioId,
        proyectoId = proyectoId
    )
}