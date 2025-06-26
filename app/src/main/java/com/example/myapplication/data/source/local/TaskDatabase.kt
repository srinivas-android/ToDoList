package com.example.myapplication.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.model.TaskResponseModel
import com.example.myapplication.data.model.TodoItem


@Database(entities = [TodoItem::class], version = 4,exportSchema = false)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun getTaskFromDao():TaskDao
}