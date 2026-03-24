package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun TommyScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    kanbanViewModel: KanbanViewModel = viewModel()
) {
    val tasks by kanbanViewModel.allTasks.collectAsState(initial = emptyList())
    var newTaskTitle by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onNavigateBack) {
                Text("Back to Home")
            }
            Text(text = "Tommy's Kanban Board", style = MaterialTheme.typography.headlineSmall)
            AsyncImage(
                model = "https://placedog.net/100/100?random",
                contentDescription = "Tommy's Dog",
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = { Text("New Kanban Task") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                if (newTaskTitle.isNotBlank()) {
                    kanbanViewModel.addTask(newTaskTitle)
                    newTaskTitle = ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KanbanColumn(
                title = "Backlog",
                tasks = tasks.filter { it.status == KanbanStatus.BACKLOG },
                modifier = Modifier.weight(1f),
                onMoveForward = { kanbanViewModel.moveTask(it, KanbanStatus.IN_PROGRESS) },
                onDelete = { kanbanViewModel.deleteTask(it) }
            )
            KanbanColumn(
                title = "In Progress",
                tasks = tasks.filter { it.status == KanbanStatus.IN_PROGRESS },
                modifier = Modifier.weight(1f),
                onMoveBack = { kanbanViewModel.moveTask(it, KanbanStatus.BACKLOG) },
                onMoveForward = { kanbanViewModel.moveTask(it, KanbanStatus.DONE) },
                onDelete = { kanbanViewModel.deleteTask(it) }
            )
            KanbanColumn(
                title = "Done",
                tasks = tasks.filter { it.status == KanbanStatus.DONE },
                modifier = Modifier.weight(1f),
                onMoveBack = { kanbanViewModel.moveTask(it, KanbanStatus.IN_PROGRESS) },
                onDelete = { kanbanViewModel.deleteTask(it) }
            )
        }
    }
}

@Composable
fun KanbanColumn(
    title: String,
    tasks: List<KanbanTask>,
    modifier: Modifier = Modifier,
    onMoveBack: ((KanbanTask) -> Unit)? = null,
    onMoveForward: ((KanbanTask) -> Unit)? = null,
    onDelete: (KanbanTask) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp)
        ) {
            items(tasks) { task ->
                KanbanTaskItem(
                    task = task,
                    onMoveBack = onMoveBack?.let { { it(task) } },
                    onMoveForward = onMoveForward?.let { { it(task) } },
                    onDelete = { onDelete(task) }
                )
            }
        }
    }
}

@Composable
fun KanbanTaskItem(
    task: KanbanTask,
    onMoveBack: (() -> Unit)?,
    onMoveForward: (() -> Unit)?,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.bodySmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (onMoveBack != null) {
                    IconButton(onClick = onMoveBack, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Move Back",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(16.dp)
                    )
                }
                if (onMoveForward != null) {
                    IconButton(onClick = onMoveForward, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Move Forward",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TommyScreenPreview() {
    MyApplicationTheme {
        TommyScreen(onNavigateBack = {})
    }
}
