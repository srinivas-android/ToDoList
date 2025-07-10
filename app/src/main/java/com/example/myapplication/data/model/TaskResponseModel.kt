package com.example.myapplication.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskResponseModel(
    val total: Int,
    val skip: Int,
    val limit: Int,
    val todos: List<TodoItem>
): Parcelable
@Parcelize
@Entity("todoTable")
data class TodoItem(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    val todo: String,
    val completed: Boolean,
    val userId: Int
): Parcelable
@Parcelize
data class UpdateTaskRequest(
    @PrimaryKey
    val todo: String,
    val completed: Boolean,
    val userId: Int
): Parcelable
