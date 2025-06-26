package com.example.myapplication.data.source.remote

import com.example.myapplication.data.model.TaskResponseModel
import com.example.myapplication.data.model.TodoItem
import com.example.myapplication.util.constants.Constants
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SetTaskApi {
    // get category names
    @GET(Constants.TODOS_ENDPOINT)
    suspend fun getAllTodos(): TaskResponseModel
    @GET(Constants.TODOS_ENDPOINT)
    suspend fun getPagedTodos(@Query("page") page: Int,
                            @Query("pageSize") pageSize: Int): TaskResponseModel

    @POST("todos/add")
    suspend fun addTask(@Body task: TodoItem): TodoItem

    @PUT("todos/{id}")
    suspend fun updateTask(
        @Path("id") id: Int,
        @Body task: TodoItem
    ): TodoItem

    @DELETE("todos/{id}")
    suspend fun deleteTask(@Path("id") id: Int): TodoItem
}