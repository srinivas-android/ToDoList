package com.example.myapplication.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.myapplication.data.model.TodoItem
import com.example.myapplication.data.source.remote.SetTaskApi
import com.example.myapplication.data.paging.TaskPaging
import com.example.myapplication.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagedTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

//    val pagedTasks = taskRepository.getPagedRemoteTasks()
//        .cachedIn(viewModelScope)

    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    private val _tasks = MutableLiveData<List<TodoItem>>()
    val tasks: LiveData<List<TodoItem>> get() = _tasks

    private val pageSize = 10
    var totalPages = 1

    fun loadPage(page: Int) {
        viewModelScope.launch {
            val result = taskRepository.getRemoteTaskPage(page, pageSize)
            result.data?.let { response ->
                _tasks.value = response.todos
                totalPages = (response.total + pageSize - 1) / pageSize
                _currentPage.value = page
            }
        }
    }

    fun nextPage() {
        val next = (_currentPage.value ?: 0) + 1
        if (next < totalPages) loadPage(next)
    }

    fun prevPage() {
        val prev = (_currentPage.value ?: 0) - 1
        if (prev >= 0) loadPage(prev)
    }

    init {
        loadPage(0)
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
}