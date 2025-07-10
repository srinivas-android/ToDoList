package com.example.myapplication.ui.task

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.TodoItem
import com.example.myapplication.data.repository.TaskRepository
import com.example.myapplication.util.ConnectivityObserver
import com.example.myapplication.util.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val connectivityObserver: ConnectivityObserver) : ViewModel() {

    private val _networkStatus = MutableStateFlow(ConnectivityObserver.Status.Available)
    val networkStatus: StateFlow<ConnectivityObserver.Status> = _networkStatus

    private val _deleteStatus = MutableLiveData<String?>()
    val deleteStatus: LiveData<String?> = _deleteStatus

    fun clearDeleteStatus() {
        _deleteStatus.value = null
    }

    private val _insertStatus = MutableLiveData<String>()
    val insertStatus: LiveData<String> get() = _insertStatus

    private val _tasks = MutableLiveData<List<TodoItem>>()
    val tasks: LiveData<List<TodoItem>> get() = _tasks

    init {

        viewModelScope.launch {

            connectivityObserver.observe().collect { status ->
                Log.e("hello",_networkStatus.value.toString())
                _networkStatus.value = status
            }
        }
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
            if (_networkStatus.value != ConnectivityObserver.Status.Available) {
                _deleteStatus.value = "No Internet Connection"
            } else {
                taskRepository.deleteRemoteTask(id)
                taskRepository.deleteLocalTask(id)
            }
        }
    }

    fun toggleCompleted(task: TodoItem) {
        val updated = task.copy(completed = !task.completed)
        viewModelScope.launch {
            if (_networkStatus.value != ConnectivityObserver.Status.Available) {
                _deleteStatus.value = "No Internet Connection"
            } else {
                taskRepository.updateRemoteTask(updated)
                taskRepository.updateLocalTask(updated)
            }
        }
    }

    fun clearInsertStatus() {
        _insertStatus.value = ""
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            if (_networkStatus.value != ConnectivityObserver.Status.Available) {
                _deleteStatus.value = "No Internet Connection"
            }
            else {
                _insertStatus.value = "Inserting..."
                val result = taskRepository.insertRemoteTask(
                    TodoItem(
                        todo = title,
                        id = 0,
                        completed = false,
                        userId = 1
                    )
                )
                if (result is Resource.Success && result.data != null) {
                    taskRepository.insertLocalTasks(listOf(result.data))
                    _insertStatus.value = "Task #${result.data.id} inserted successfully!"
                } else {
                    _insertStatus.value = "Failed to insert task: ${result.message}"
                }
            }
        }
    }
}
