package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface KanbanDao {
    @Query("SELECT * FROM kanban_tasks")
    fun getAllTasks(): Flow<List<KanbanTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: KanbanTask)

    @Update
    suspend fun updateTask(task: KanbanTask)

    @Delete
    suspend fun deleteTask(task: KanbanTask)
}
