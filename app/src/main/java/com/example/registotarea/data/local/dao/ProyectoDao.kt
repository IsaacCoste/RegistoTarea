package com.example.registotarea.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.registotarea.data.local.entities.ProyectoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProyectoDao {
    @Upsert()
    suspend fun save(proyecto: ProyectoEntity)

    @Query(
        """
        SELECT * FROM Proyectos
        WHERE proyectoId = :proyectoId
        LIMIT 1
    """
    )
    suspend fun getProyecto(proyectoId: Int): ProyectoEntity?

    @Delete
    suspend fun delete(proyecto: ProyectoEntity)

    @Query("SELECT * FROM Proyectos")
    fun getProyectos(): Flow<List<ProyectoEntity>>
}