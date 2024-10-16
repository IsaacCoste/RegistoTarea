package com.example.registotarea.data.remote

import com.example.registotarea.data.remote.dto.ProyectoDto
import com.example.registotarea.data.remote.dto.TareaDto
import com.example.registotarea.data.remote.dto.UsuarioDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TareaApi {
    @GET("api/Usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): UsuarioDto

    @GET("api/Usuarios")
    suspend fun getUsuarios(): List<UsuarioDto>

    @POST("api/Usuarios")
    suspend fun saveUsuario(@Body usuario: UsuarioDto): UsuarioDto

    @DELETE("api/Usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Void?>


    @GET("api/Tareas/{id}")
    suspend fun getTarea(@Path("id") id: Int): TareaDto

    @GET("api/Tareas")
    suspend fun getTareas(): List<TareaDto>

    @POST("api/Tareas")
    suspend fun saveTarea(@Body tarea: TareaDto): TareaDto

    @DELETE("api/Tareas/{id}")
    suspend fun deleteTarea(@Path("id") id: Int): Response<Void?>


    @GET("api/Proyectos/{id}")
    suspend fun getProyecto(@Path("id") id: Int): ProyectoDto

    @GET("api/Proyectos")
    suspend fun getProyectos(): List<ProyectoDto>

    @POST("api/Proyectos")
    suspend fun saveProyecto(@Body proyecto: ProyectoDto): ProyectoDto

    @DELETE("api/Proyectos/{id}")
    suspend fun deleteProyecto(@Path("id") id: Int): Response<Void?>
}