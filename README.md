# ToDoList

✅ Task Management

Add New Task

Add a new task using the input field.

Task is saved both remotely (via API) and locally (Room DB).

Displays a confirmation snackbar with task ID and result status.

View Tasks with Pagination

Paginated display of tasks (10 per page).

Navigation through pages using “Previous” and “Next” buttons.

Dynamic header (“My Tasks”) shown only when tasks are available.

Update Task Completion Status

Toggle a task’s completed state using a checkbox.

Updates are reflected both remotely and locally.

Delete Task

Remove tasks via delete icon.

Updates local and remote databases.

📲 Data Handling & Synchronization
Remote API Integration

Fetches tasks from a RESTful API using Retrofit.

Uses page and pageSize parameters for efficient backend paging.

Room Local Database

Stores fetched and user-inserted tasks locally for offline access.

Auto-syncs Room DB with latest server data on refresh.

Manual Sync Button

A Refresh button lets users manually pull latest tasks from the server.

💉 Dependency Injection (DI)
Hilt Integration

Injects ViewModel and repository instances.

@HiltViewModel and @AndroidEntryPoint used where needed.

🖼️ Elegant UI (Jetpack Compose)
Modern, clean UI built with Jetpack Compose.

Responsive layout using LazyColumn for task list.

Pagination controls and snackbars with visual status cues.

Form input aligned with task table for seamless UX.

📦 State Management
LiveData & State Observers

LiveData used to observe tasks and insertion status.

Compose observes changes reactively with observeAsState.

