package com.example.registotarea.data.repository

import com.example.registotarea.data.local.dao.UsuarioDao
import com.example.registotarea.data.local.entities.UsuarioEntity
import com.example.registotarea.data.remote.dto.UsuarioDto
import com.example.registotarea.data.remote.dto.toEntity
import com.example.registotarea.data.remote.RemoteDataSource
import com.example.registotarea.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val usuarioDao: UsuarioDao
) {
    fun getUsuarios(): Flow<Resource<List<UsuarioEntity>>> = flow {
        try {
            emit(Resource.Loading())

            val usuariosApi = remoteDataSource.getUsuarios()

            val usuariosRoom = usuariosApi.map { it.toEntity() }
            usuariosRoom.forEach { usuarioDao.save(it) }

            emit(Resource.Success(usuariosRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            val usuariosRoom = usuarioDao.getUsuarios().firstOrNull()
            if (usuariosRoom.isNullOrEmpty()) {
                emit(Resource.Error("No se encontraron datos locales"))
            } else {
                emit(Resource.Success(usuariosRoom))
            }
        }
    }

    fun getUsuario(id: Int): Flow<Resource<UsuarioEntity>> = flow {
        try {
            emit(Resource.Loading())
            val usuario = usuarioDao.getUsuario(id)
            emit(Resource.Success(usuario!!))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error: ${e.message}"))
        }
    }

    fun saveUsuario(usuario: UsuarioDto): Flow<Resource<UsuarioEntity>> = flow {
        try {
            emit(Resource.Loading())
            val usuarioApi = remoteDataSource.saveUsuario(usuario)
            val usuarioRoom = usuarioApi.toEntity()
            usuarioDao.save(usuarioRoom)

            emit(Resource.Success(usuarioRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error: ${e.message}"))
        }
    }

    suspend fun deleteUsuario(id: Int): Resource<Unit> {
        return try {
            remoteDataSource.deleteUsuario(id)
            val usuarioRoom = usuarioDao.getUsuario(id)
            if (usuarioRoom != null) {
                usuarioDao.delete(usuarioRoom)
            }

            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            val usuarioRoom = usuarioDao.getUsuario(id)
            if (usuarioRoom != null) {
                usuarioDao.delete(usuarioRoom)
            }
            Resource.Success(Unit)
        }
    }
}
