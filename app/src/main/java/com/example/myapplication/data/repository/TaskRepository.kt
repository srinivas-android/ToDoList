package com.example.myapplication.data.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myapplication.data.model.TaskResponseModel
import com.example.myapplication.data.source.remote.SetTaskApi
import com.example.myapplication.data.model.TodoItem
import com.example.myapplication.data.model.UpdateTaskRequest
import com.example.myapplication.data.paging.TaskPaging
import com.example.myapplication.data.source.local.TaskDao
import com.example.myapplication.util.resource.Resource
import com.example.myapplication.util.resource.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val api: SetTaskApi, private val db: TaskDao) {


    suspend fun getRemoteTask()= try {
        Resource.Success(api.getAllTodos())
    } catch (e: Exception) {
        Resource.Error(e.message.orEmpty())
    }

    suspend fun insertRemoteTasks(task: TodoItem): TodoItem = api.addTask(task)

    suspend fun updateRemoteTask(task: TodoItem): TodoItem  {
        val request = UpdateTaskRequest(
            todo = task.todo,
            completed = task.completed,
            userId = task.userId
        )
        return api.updateTask(task.id, request)
    }

    suspend fun deleteRemoteTask(id: Int): TodoItem = api.deleteTask(id)

    fun getLocalTasks(): Flow<List<TodoItem>> = db.getTasks()

    suspend fun insertLocalTasks(tasks: List<TodoItem>) = db.addTasks(tasks)

//    suspend fun insertLocalTask(task: TodoItem) = db.addTask(task)

    suspend fun updateLocalTask(task: TodoItem) = db.updateTask(task)

    suspend fun deleteLocalTask(id: Int) = db.deleteTask(id)

    suspend fun clearLocalTasks() = db.clearTask()

    fun getPagedRemoteTasks(): Flow<PagingData<TodoItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TaskPaging(api) }
        ).flow
    }

    suspend fun getRemoteTaskPage(page: Int, pageSize: Int): Resource<TaskResponseModel> {
        return safeApiCall {
            api.getPagedTodos(page, pageSize)
        }
    }

    suspend fun insertRemoteTask(task: TodoItem): Resource<TodoItem> {
        return safeApiCall {
            api.addTask(task)
        }
    }
}