package com.example.myapplication

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailsPage(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel = viewModel()
) {
    val todos by todoViewModel.allTodos.collectAsState(initial = emptyList())
    var newTaskText by remember { mutableStateOf("") }

    val neonCyan = Color(0xFF00FFFF)
    val neonMagenta = Color(0xFFFF00FF)
    val neonGreen = Color(0xFF00FF00)

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "TASK_MANIFEST.sh",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = neonCyan
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = neonCyan
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = neonCyan
                    )
                )
                Canvas(modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)) {
                    drawLine(
                        color = neonCyan.copy(alpha = 0.3f),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
        },
        containerColor = Color.Black,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "> EXECUTING QUERY: ALL_TASKS",
                style = MaterialTheme.typography.labelLarge,
                color = neonGreen,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTaskText,
                    onValueChange = { newTaskText = it },
                    label = { Text("APPEND_NEW_TASK", fontFamily = FontFamily.Monospace) },
                    modifier = Modifier.weight(1f),
                    shape = CutCornerShape(bottomEnd = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = neonMagenta,
                        unfocusedBorderColor = neonMagenta.copy(alpha = 0.5f),
                        focusedLabelColor = neonMagenta,
                        unfocusedLabelColor = neonMagenta.copy(alpha = 0.5f),
                        cursorColor = neonMagenta,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = {
                        if (newTaskText.isNotBlank()) {
                            todoViewModel.addTodo(newTaskText)
                            newTaskText = ""
                        }
                    },
                    modifier = Modifier
                        .background(neonMagenta.copy(alpha = 0.1f), CutCornerShape(4.dp))
                        .border(1.dp, neonMagenta, CutCornerShape(4.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add task", tint = neonMagenta)
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(todos) { todo ->
                    HackerTodoItem(
                        todo = todo,
                        onCheckedChange = { isChecked ->
                            todoViewModel.updateTodo(todo.copy(isDone = isChecked))
                        },
                        onDelete = {
                            todoViewModel.deleteTodo(todo)
                        },
                        neonCyan = neonCyan,
                        neonGreen = neonGreen
                    )
                }
            }

            Text(
                text = "SYSTEM_LOG: ${todos.size} ENTRIES LOADED",
                color = neonCyan.copy(alpha = 0.5f),
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun HackerTodoItem(
    todo: Todo,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    neonCyan: Color,
    neonGreen: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, neonCyan.copy(alpha = 0.2f), CutCornerShape(bottomEnd = 8.dp))
            .background(Color(0xFF0A0A0A), CutCornerShape(bottomEnd = 8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = neonGreen,
                uncheckedColor = neonCyan.copy(alpha = 0.5f),
                checkmarkColor = Color.Black
            )
        )
        Text(
            text = if (todo.isDone) "[RESOLVED] ${todo.task}" else "[ACTIVE] ${todo.task}",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace,
            color = if (todo.isDone) neonGreen else Color.White,
            fontWeight = if (!todo.isDone) FontWeight.Bold else FontWeight.Normal
        )
        IconButton(onClick = onDelete) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete task",
                tint = Color.Red.copy(alpha = 0.7f)
            )
        }
    }
}
