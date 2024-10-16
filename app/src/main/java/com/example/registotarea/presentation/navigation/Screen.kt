package com.example.registotarea.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object TareaList : Screen()
    @Serializable
    data class Tarea(val id: Int, val isTareaDelete: Boolean) : Screen()
    @Serializable
    data object UsuarioList : Screen()
    @Serializable
    data class Usuario(val usuarioId: Int, val isUsuarioDelete: Boolean) : Screen()
    @Serializable
    data object ProyectoList : Screen()
    @Serializable
    data class Proyecto(val id: Int, val isProyectoDelete: Boolean) : Screen()
    @Serializable
    data object HomeScreen : Screen()

}