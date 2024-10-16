package com.example.registotarea.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.registotarea.data.local.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Upsert()
    suspend fun save(usuario: UsuarioEntity)

    @Query(
        """
        SELECT * FROM usuarios
        WHERE usuarioId = :usuarioId
        LIMIT 1
    """
    )
    suspend fun getUsuario(usuarioId: Int): UsuarioEntity?

    @Delete
    suspend fun delete(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios")
    fun getUsuarios(): Flow<List<UsuarioEntity>>
}