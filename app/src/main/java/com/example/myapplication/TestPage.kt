package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

enum class PieceType { RED, BLACK }
data class Position(val row: Int, val col: Int)
data class Piece(val type: PieceType, val isKing: Boolean = false)

@Composable
fun TestPage(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CheckersViewModel = viewModel()
) {
    val board = viewModel.board
    val selectedPosition = viewModel.selectedPosition
    val currentTurn = viewModel.currentTurn
    val message = viewModel.message

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Checkers",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = if (currentTurn == PieceType.BLACK) Color.Black else Color.Red
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Board
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(2.dp, Color.DarkGray)
            ) {
                Column {
                    for (row in 0 until 8) {
                        Row(modifier = Modifier.weight(1f)) {
                            for (col in 0 until 8) {
                                val pos = Position(row, col)
                                val isDark = (row + col) % 2 != 0
                                BoardSquare(
                                    pos = pos,
                                    isDark = isDark,
                                    piece = board[pos],
                                    isSelected = selectedPosition == pos,
                                    onClick = { viewModel.handleSquareClick(pos) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = { viewModel.resetGame() }) {
                    Text("Reset")
                }
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedButton(onClick = onNavigateBack) {
                    Text("Back")
                }
            }
        }
    }
}

@Composable
fun BoardSquare(
    pos: Position,
    isDark: Boolean,
    piece: Piece?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) Color(0xFF769656) else Color(0xFFEEEED2))
            .clickable(enabled = isDark) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow.copy(alpha = 0.4f))
            )
        }

        if (piece != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .clip(CircleShape)
                    .background(if (piece.type == PieceType.BLACK) Color.Black else Color.Red)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (piece.isKing) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "King",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

fun initialBoard(): Map<Position, Piece> {
    val board = mutableMapOf<Position, Piece>()
    for (row in 0 until 8) {
        for (col in 0 until 8) {
            if ((row + col) % 2 != 0) {
                if (row < 3) {
                    board[Position(row, col)] = Piece(PieceType.RED)
                } else if (row > 4) {
                    board[Position(row, col)] = Piece(PieceType.BLACK)
                }
            }
        }
    }
    return board
}

@Preview(showBackground = true)
@Composable
fun TestPagePreview() {
    MyApplicationTheme {
        TestPage(onNavigateBack = {})
    }
}
