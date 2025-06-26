package com.example.myapplication.ui.task

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.TodoItem
import com.example.myapplication.data.repository.TaskRepository
import com.example.myapplication.util.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {

    private val _insertStatus = MutableLiveData<String>()
    val insertStatus: LiveData<String> get() = _insertStatus

    private val _tasks = MutableLiveData<List<TodoItem>>()
    val tasks: LiveData<List<TodoItem>> get() = _tasks

    init {
        observeLocalTasks()
    }

    private fun observeLocalTasks() {
        viewModelScope.launch {
            taskRepository.getLocalTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun refreshFromRemote() {
        viewModelScope.launch {
            when (val result = taskRepository.getRemoteTask()) {
                is Resource.Success -> {
                    result.data?.let { taskResponse ->
                        taskRepository.clearLocalTasks()
                        taskRepository.insertLocalTasks(taskResponse.todos)
                    }
                }
                is Resource.Error -> {
                    Log.e("TaskViewModel", "Failed to fetch: ${result.message}")
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            taskRepository.deleteRemoteTask(id)
            taskRepository.deleteLocalTask(id)
        }
    }

    fun toggleCompleted(task: TodoItem) {
        val updated = task.copy(completed = !task.completed)
        viewModelScope.launch {
            taskRepository.updateRemoteTask(updated)
            taskRepository.updateLocalTask(updated)
        }
    }

    fun clearInsertStatus() {
        _insertStatus.value = ""
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            _insertStatus.value = "Inserting..."
            val result = taskRepository.insertRemoteTask(TodoItem(todo = title,
                id = 0,
                completed = false,
                userId = 1 ))
            if (result is Resource.Success && result.data != null) {
                taskRepository.insertLocalTasks(listOf(result.data))
                _insertStatus.value = "Task #${result.data.id} inserted successfully!"
            } else {
                _insertStatus.value = "Failed to insert task: ${result.message}"
            }
        }
    }
}
