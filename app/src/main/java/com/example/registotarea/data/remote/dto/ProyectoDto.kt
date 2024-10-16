package com.example.registotarea.data.remote.dto

import com.example.registotarea.data.local.entities.ProyectoEntity

class ProyectoDto(
    val proyectoId: Int,
    val nombre: String,
    val descripcion: String,
    val presupuesto: Double
)

fun ProyectoDto.toEntity(): ProyectoEntity {
    return ProyectoEntity(
        proyectoId = proyectoId,
        nombre = nombre,
        descripcion = descripcion,
        presupuesto = presupuesto
    )
}