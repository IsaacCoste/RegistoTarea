package com.example.registotarea.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.registotarea.data.local.entities.TareaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    @Upsert()
    suspend fun save(tarea: TareaEntity)

    @Query(
        """
        SELECT * FROM tareas
        WHERE tareaId = :tareaId
        LIMIT 1
        """
    )
    suspend fun getTarea(tareaId: Int): TareaEntity?

    @Delete
    suspend fun delete(tarea: TareaEntity)

    @Query("SELECT * FROM tareas")
    fun getTareas(): Flow<List<TareaEntity>>
}