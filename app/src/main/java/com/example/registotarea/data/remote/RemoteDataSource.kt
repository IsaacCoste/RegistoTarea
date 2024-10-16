package com.example.registotarea.data.remote

import com.example.registotarea.data.remote.dto.ProyectoDto
import com.example.registotarea.data.remote.dto.TareaDto
import com.example.registotarea.data.remote.dto.UsuarioDto
import javax.inject.Inject

class RemoteDataSource@Inject constructor(
    private val tareaApi: TareaApi
){
    suspend fun getUsuarios() = tareaApi.getUsuarios()
    suspend fun getUsuario(id: Int) = tareaApi.getUsuario(id)
    suspend fun saveUsuario(usuario: UsuarioDto) = tareaApi.saveUsuario(usuario)
    suspend fun deleteUsuario(id: Int) = tareaApi.deleteUsuario(id)


    suspend fun getProyectos() = tareaApi.getProyectos()
    suspend fun getProyecto(id: Int) = tareaApi.getProyecto(id)
    suspend fun saveProyecto(proyecto: ProyectoDto) = tareaApi.saveProyecto(proyecto)
    suspend fun deleteProyecto(id: Int) = tareaApi.deleteProyecto(id)


    suspend fun getTareas() = tareaApi.getTareas()
    suspend fun getTarea(id: Int) = tareaApi.getTarea(id)
    suspend fun saveTarea(tarea: TareaDto) = tareaApi.saveTarea(tarea)
    suspend fun deleteTarea(id: Int) = tareaApi.deleteTarea(id)
}