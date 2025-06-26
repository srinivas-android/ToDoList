package com.example.myapplication.ui.task

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.shared.TaskListItem

@Composable
fun TaskListScreen(viewModel: TaskViewModel = hiltViewModel()) {
    val tasks by viewModel.tasks.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        if (tasks.isEmpty()) {
            Text("No tasks found locally.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.refreshFromRemote() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Refresh from Server")
            }
        } else {
            TaskInsertForm(viewModel)
            TaskDataTable(
                tasks = tasks,
                onToggleComplete = { viewModel.toggleCompleted(it) },
                onDelete = { viewModel.deleteTask(it.id) }
            )
        }
    }
}

@Composable
fun TaskInsertForm(viewModel: TaskViewModel) {
    var text by remember { mutableStateOf("") }
    val insertStatus: String by viewModel.insertStatus.observeAsState("")
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(insertStatus) {
        if (insertStatus.isNotBlank()) {
            snackbarHostState.showSnackbar(insertStatus)
            viewModel.clearInsertStatus()
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("New Task") },
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(8.dp))

            Button(onClick = {
                if (text.isNotBlank()) {
                    viewModel.addTask(text)
                    text = ""
                }
            }) {
                Text("Add")
            }
        }

        /*if (insertStatus.isNotBlank()) {
            val isSuccess = insertStatus?.contains("success", ignoreCase = true) == true

            Text(
                text = insertStatus ?: "",
                color = if (isSuccess) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
         */
        Spacer(modifier = Modifier.height(8.dp))
        SnackbarHost(hostState = snackbarHostState)
    }
}