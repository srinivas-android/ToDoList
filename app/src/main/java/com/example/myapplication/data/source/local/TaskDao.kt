package com.example.myapplication.data.source.local

import androidx.room.*
import com.example.myapplication.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addTask(Task: List<TodoItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTasks(tasks: List<TodoItem>)

    @Query("SELECT * FROM todoTable")
    fun getTasks(): Flow<List<TodoItem>>

    @Update
    suspend fun updateTask(task: TodoItem)

    @Query("DELETE FROM todoTable WHERE id=:id")
    suspend fun deleteTask(id:Int)

    @Query("DELETE FROM todoTable")
    suspend fun clearTask()

}