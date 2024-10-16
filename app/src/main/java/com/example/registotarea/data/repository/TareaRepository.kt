package com.example.registotarea.data.repository

import android.util.Log
import com.example.registotarea.data.local.dao.TareaDao
import com.example.registotarea.data.local.entities.TareaEntity
import com.example.registotarea.data.remote.RemoteDataSource
import com.example.registotarea.data.remote.dto.TareaDto
import com.example.registotarea.data.remote.dto.toEntity
import com.example.registotarea.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class TareaRepository @Inject constructor(
    private val remoteTarea: RemoteDataSource,
    private val tareaDao: TareaDao
) {
    fun getTareas(): Flow<Resource<List<TareaEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val tareasApi = remoteTarea.getTareas()
            val tareasRoom = tareasApi.map { it.toEntity() }
            tareasRoom.forEach { tareaDao.save(it) }

            emit(Resource.Success(tareasRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message()}"))
        } catch (e: Exception) {
            val tareasRoom = tareaDao.getTareas().firstOrNull()
            if (tareasRoom.isNullOrEmpty()) {
                emit(Resource.Error(e.message ?: "Error desconocido"))
            } else {
                emit(Resource.Success(tareasRoom))
            }
        }
    }

    fun getTarea(id: Int): Flow<Resource<TareaEntity>> = flow {
        try {
            emit(Resource.Loading())
            val tarea = tareaDao.getTarea(id)
            emit(Resource.Success(tarea!!))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message()}"))
        } catch (e: Exception) {
            Log.e("TareaRepository", "getTarea: ${e.message}")
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }
    fun saveTarea(tarea: TareaDto): Flow<Resource<TareaEntity>> = flow {
        try {
            emit(Resource.Loading())
            val tareaApi = remoteTarea.saveTarea(tarea)
            val tareaRoom = tareaApi.toEntity()
            tareaDao.save(tareaRoom)
            emit(Resource.Success(tareaRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error: ${e.message}"))
        }
    }
    suspend fun deleteTarea(id: Int): Resource<Unit> {
        return try {
            remoteTarea.deleteTarea(id)
            val tareaRoom = tareaDao.getTarea(id)
            if (tareaRoom != null) {
                tareaDao.delete(tareaRoom)
            }
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            val tareaRoom = tareaDao.getTarea(id)
            if (tareaRoom != null) {
                tareaDao.delete(tareaRoom)
            }
            Resource.Success(Unit)
        }
    }
}