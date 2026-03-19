package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(true) }
            
            MyApplicationTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            })
                        }
                        composable("home") {
                            Greeting(
                                name = "Android",
                                isDarkMode = isDarkMode,
                                onDarkModeChange = { isDarkMode = it },
                                onNavigateToTommy = { navController.navigate("tommy") },
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("tommy") {
                            TommyScreen(onNavigateBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FerrisWheel(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "FerrisWheelRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing)
        ),
        label = "Rotation"
    )

    val mainColor = MaterialTheme.colorScheme.primary
    val cabinColor = MaterialTheme.colorScheme.secondary

    Canvas(modifier = modifier.size(200.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2 * 0.8f
        
        // Draw main structure
        drawCircle(
            color = mainColor,
            radius = radius,
            center = center,
            style = Stroke(width = 4.dp.toPx())
        )
        
        // Draw spokes and cabins
        rotate(rotation, pivot = center) {
            for (i in 0 until 8) {
                val angle = (i * 45f) * (Math.PI / 180f).toFloat()
                val stopX = center.x + radius * cos(angle)
                val stopY = center.y + radius * sin(angle)
                
                drawLine(
                    color = mainColor,
                    start = center,
                    end = Offset(stopX, stopY),
                    strokeWidth = 2.dp.toPx()
                )
                
                // Draw cabin (staying upright)
                rotate(-rotation - (i * 45f), pivot = Offset(stopX, stopY)) {
                    drawRect(
                        color = cabinColor,
                        topLeft = Offset(stopX - 10.dp.toPx(), stopY - 5.dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(20.dp.toPx(), 20.dp.toPx())
                    )
                }
            }
        }
        
        // Support structure
        drawLine(
            color = mainColor,
            start = center,
            end = Offset(center.x - radius, size.height),
            strokeWidth = 4.dp.toPx()
        )
        drawLine(
            color = mainColor,
            start = center,
            end = Offset(center.x + radius, size.height),
            strokeWidth = 4.dp.toPx()
        )
    }
}

@Composable
fun Greeting(
    name: String,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onNavigateToTommy: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel = viewModel(),
    wealthViewModel: WealthViewModel = viewModel()
) {
    val todos by todoViewModel.allTodos.collectAsState(initial = emptyList())
    val balance by wealthViewModel.currentBalance.collectAsState(initial = 0.0)
    var newTaskText by remember { mutableStateOf("") }
    var showStockPopup by remember { mutableStateOf(false) }

    if (showStockPopup) {
        AlertDialog(
            onDismissRequest = { showStockPopup = false },
            title = { Text(text = "Stock Market Update") },
            text = {
                Column {
                    Text("STONKS ARE UP! 🚀")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("GMNI: +420.69%")
                    Text("TMY: +100.00%")
                    Text("TODO: +12.5% (Invest in your productivity!)")
                }
            },
            confirmButton = {
                TextButton(onClick = { showStockPopup = false }) {
                    Text("To the moon!")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://placedog.net/100/100?random",
                contentDescription = "Cool Dog",
                modifier = Modifier.size(60.dp)
            )
            
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Wealth Balance", style = MaterialTheme.typography.labelSmall)
                Text(
                    text = NumberFormat.getCurrencyInstance(Locale.US).format(balance),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        FerrisWheel(modifier = Modifier.padding(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Dark Mode")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = isDarkMode, onCheckedChange = onDarkModeChange)
        }

        Text(text = "Hello $name!", style = MaterialTheme.typography.headlineMedium)
        
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onNavigateToTommy, modifier = Modifier.weight(1f)) {
                Text("Tommy's Page")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = onLogout, modifier = Modifier.weight(1f)) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                wealthViewModel.addAMillion() 
                showStockPopup = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700), contentColor = Color.Black)
        ) {
            Icon(Icons.Default.Star, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("MAKE ME RICH")
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Your Todo List", style = MaterialTheme.typography.titleLarge)
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTaskText,
                onValueChange = { newTaskText = it },
                label = { Text("New task") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                if (newTaskText.isNotBlank()) {
                    todoViewModel.addTodo(newTaskText)
                    newTaskText = ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(todos) { todo ->
                TodoItem(
                    todo = todo,
                    onCheckedChange = { isChecked ->
                        todoViewModel.updateTodo(todo.copy(isDone = isChecked))
                    },
                    onDelete = {
                        todoViewModel.deleteTodo(todo)
                    }
                )
            }
        }
    }
}

@Composable
fun TodoItem(todo: Todo, onCheckedChange: (Boolean) -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = todo.isDone, onCheckedChange = onCheckedChange)
        Text(
            text = todo.task,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete task")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting(
            name = "Android",
            isDarkMode = false,
            onDarkModeChange = {},
            onNavigateToTommy = {},
            onLogout = {}
        )
    }
}
