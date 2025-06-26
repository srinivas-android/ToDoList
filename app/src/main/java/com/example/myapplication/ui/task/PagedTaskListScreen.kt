package com.example.myapplication.ui.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myapplication.ui.shared.TaskListItem

/*@Composable
fun PagedTaskListScreen(viewModel: PagedTaskViewModel = hiltViewModel()) {
    val taskItems = viewModel.pagedTasks.collectAsLazyPagingItems()

    LazyColumn {
        items(taskItems.itemCount) { index ->
            val task = taskItems[index]
            task?.let {
                TaskListItem(
                    task = it,
                    onDelete = { viewModel.deleteTask(it.id) },
                    onToggleComplete = { viewModel.toggleCompleted(it) }
                )
            }
        }


        taskItems.apply {
            when {
                loadState.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }

                loadState.append is LoadState.Error -> {
                    val error = loadState.append as LoadState.Error
                    item {
                        RetryItem(message = error.error.localizedMessage ?: "Unknown error") {
                            retry()
                        }
                    }
                }
            }

            // Optional: Show initial loading
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                        )
                    }
                }

                is LoadState.Error -> {
                    val error = loadState.refresh as LoadState.Error
                    item {
                        RetryItem(message = error.error.localizedMessage ?: "Unknown error") {
                            retry()
                        }
                    }
                }

                else -> Unit
            }
        }
    }
}
@Composable
fun RetryItem(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = Color.Red)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
*/