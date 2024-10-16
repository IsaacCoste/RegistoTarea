package com.example.registotarea.data.repository

import android.util.Log
import com.example.registotarea.data.local.dao.ProyectoDao
import com.example.registotarea.data.local.entities.ProyectoEntity
import com.example.registotarea.data.remote.RemoteDataSource
import com.example.registotarea.data.remote.dto.ProyectoDto
import com.example.registotarea.data.remote.dto.toEntity
import com.example.registotarea.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ProyectoRepository @Inject constructor(
    private val remoteProyecto: RemoteDataSource,
    private val proyectoDao: ProyectoDao
){
    fun getProyectos(): Flow<Resource<List<ProyectoEntity>>> = flow{
        try {
            emit(Resource.Loading())
            val proyectosApi = remoteProyecto.getProyectos()
            val proyectosRoom = proyectosApi.map { it.toEntity() }
            proyectosRoom.forEach { proyectoDao.save(it) }

            emit(Resource.Success(proyectosRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message()}"))
            }
        catch (e: Exception) {
            Log.e("ProyectoRepository", "getProyectos: ${e.message}")
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }
    fun getProyecto(id: Int): Flow<Resource<ProyectoEntity>> = flow {
        try {
            emit(Resource.Loading())
            val proyecto = proyectoDao.getProyecto(id)
            emit(Resource.Success(proyecto!!))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message()}"))
        } catch (e: Exception) {
            Log.e("ProyectoRepository", "getProyecto: ${e.message}")
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }
    fun saveProyecto(proyecto: ProyectoDto): Flow<Resource<ProyectoEntity>> = flow {
        try {
            emit(Resource.Loading())
            val proyectoApi = remoteProyecto.saveProyecto(proyecto)
            val proyectoRoom = proyectoApi.toEntity()
            proyectoDao.save(proyectoRoom)

            emit(Resource.Success(proyectoRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error: ${e.message}"))
        }
    }
    suspend fun deleteProyecto(id: Int): Resource<Unit> {
        return try {
            remoteProyecto.deleteProyecto(id)
            val proyectoRoom = proyectoDao.getProyecto(id)
            if (proyectoRoom != null) {
                proyectoDao.delete(proyectoRoom)
            }

            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            val proyectoRoom = proyectoDao.getProyecto(id)
            if (proyectoRoom != null) {
                proyectoDao.delete(proyectoRoom)
            }
            Resource.Success(Unit)
        }
    }
}