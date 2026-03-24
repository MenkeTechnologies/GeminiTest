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
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnakeScreen(
    onNavigateBack: () -> Unit,
    viewModel: SnakeViewModel = viewModel()
) {
    val neonCyan = Color(0xFF00FFFF)
    val neonMagenta = Color(0xFFFF00FF)
    val neonGreen = Color(0xFF00FF00)

    val snake = viewModel.snake
    val food = viewModel.food
    val isGameOver = viewModel.isGameOver
    val score = viewModel.score
    val gridSize = viewModel.gridSize

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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = neonCyan
                        )
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
                            Text(
                                "CORE_CRITICAL: GAME OVER",
                                color = Color.Red,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.resetGame() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = neonCyan,
                                    contentColor = Color.Black
                                ),
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
                    onClick = { viewModel.updateDirection(Direction.UP) },
                    modifier = Modifier
                        .border(1.dp, neonCyan, CutCornerShape(4.dp))
                        .size(64.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "Up",
                        tint = neonCyan,
                        modifier = Modifier.size(48.dp)
                    )
                }
                Row {
                    IconButton(
                        onClick = { viewModel.updateDirection(Direction.LEFT) },
                        modifier = Modifier
                            .border(1.dp, neonCyan, CutCornerShape(4.dp))
                            .size(64.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Left",
                            tint = neonCyan,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(64.dp))
                    IconButton(
                        onClick = { viewModel.updateDirection(Direction.RIGHT) },
                        modifier = Modifier
                            .border(1.dp, neonCyan, CutCornerShape(4.dp))
                            .size(64.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Right",
                            tint = neonCyan,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
                IconButton(
                    onClick = { viewModel.updateDirection(Direction.DOWN) },
                    modifier = Modifier
                        .border(1.dp, neonCyan, CutCornerShape(4.dp))
                        .size(64.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Down",
                        tint = neonCyan,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}
