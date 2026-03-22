package com.example.myapplication

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class Direction { UP, DOWN, LEFT, RIGHT }
data class SnakePoint(val x: Int, val y: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnakeScreen(onNavigateBack: () -> Unit) {
    val neonCyan = Color(0xFF00FFFF)
    val neonMagenta = Color(0xFFFF00FF)
    val neonGreen = Color(0xFF00FF00)

    val gridSize = 20
    var snake by remember { mutableStateOf(listOf(SnakePoint(5, 10), SnakePoint(4, 10), SnakePoint(3, 10))) }
    var food by remember { mutableStateOf(SnakePoint(15, 10)) }
    var direction by remember { mutableStateOf(Direction.RIGHT) }
    var isGameOver by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }

    LaunchedEffect(isGameOver) {
        while (!isGameOver) {
            delay(150)
            val head = snake.first()
            val newHead = when (direction) {
                Direction.UP -> SnakePoint(head.x, (head.y - 1 + gridSize) % gridSize)
                Direction.DOWN -> SnakePoint(head.x, (head.y + 1) % gridSize)
                Direction.LEFT -> SnakePoint((head.x - 1 + gridSize) % gridSize, head.y)
                Direction.RIGHT -> SnakePoint((head.x + 1) % gridSize, head.y)
            }

            if (snake.contains(newHead)) {
                isGameOver = true
            } else {
                val newSnake = (mutableListOf(newHead) + snake).toMutableList()
                if (newHead == food) {
                    score += 10
                    food = SnakePoint(Random.nextInt(gridSize), Random.nextInt(gridSize))
                } else {
                    newSnake.removeAt(newSnake.size - 1)
                }
                snake = newSnake
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "SNAKE_PROTOCOL.exe",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = neonCyan
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = neonCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "SCORE: $score",
                color = neonGreen,
                fontFamily = FontFamily.Monospace,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Game Board
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .border(2.dp, neonCyan, CutCornerShape(8.dp))
                    .background(Color(0xFF0A0A0A))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cellSize = size.width / gridSize
                    
                    // Draw Food
                    drawRect(
                        color = neonMagenta,
                        topLeft = Offset(food.x * cellSize, food.y * cellSize),
                        size = Size(cellSize, cellSize)
                    )

                    // Draw Snake
                    snake.forEachIndexed { index, point ->
                        drawRect(
                            color = if (index == 0) neonGreen else neonCyan,
                            topLeft = Offset(point.x * cellSize, point.y * cellSize),
                            size = Size(cellSize, cellSize)
                        )
                    }
                }

                if (isGameOver) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.8f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("CORE_CRITICAL: GAME OVER", color = Color.Red, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    snake = listOf(SnakePoint(5, 10), SnakePoint(4, 10), SnakePoint(3, 10))
                                    direction = Direction.RIGHT
                                    isGameOver = false
                                    score = 0
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = neonCyan, contentColor = Color.Black),
                                shape = CutCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Text(" REBOOT_SYSTEM", fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Controls
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { if (direction != Direction.DOWN) direction = Direction.UP },
                    modifier = Modifier.border(1.dp, neonCyan, CutCornerShape(4.dp)).size(64.dp)
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up", tint = neonCyan, modifier = Modifier.size(48.dp))
                }
                Row {
                    IconButton(
                        onClick = { if (direction != Direction.RIGHT) direction = Direction.LEFT },
                        modifier = Modifier.border(1.dp, neonCyan, CutCornerShape(4.dp)).size(64.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Left", tint = neonCyan, modifier = Modifier.size(48.dp))
                    }
                    Spacer(modifier = Modifier.width(64.dp))
                    IconButton(
                        onClick = { if (direction != Direction.LEFT) direction = Direction.RIGHT },
                        modifier = Modifier.border(1.dp, neonCyan, CutCornerShape(4.dp)).size(64.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Right", tint = neonCyan, modifier = Modifier.size(48.dp))
                    }
                }
                IconButton(
                    onClick = { if (direction != Direction.UP) direction = Direction.DOWN },
                    modifier = Modifier.border(1.dp, neonCyan, CutCornerShape(4.dp)).size(64.dp)
                ) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down", tint = neonCyan, modifier = Modifier.size(48.dp))
                }
            }
        }
    }
}
