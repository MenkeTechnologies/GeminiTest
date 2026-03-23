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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

enum class ChessPieceColor { WHITE, BLACK }
enum class ChessPieceType { PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING }
data class ChessPiece(val type: ChessPieceType, val color: ChessPieceColor)

@Composable
fun ChessPage(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChessViewModel = viewModel()
) {
    val board = viewModel.board
    val selectedPosition = viewModel.selectedPosition
    val currentTurn = viewModel.currentTurn

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
                text = "Chess",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${currentTurn.name.lowercase().replaceFirstChar { it.uppercase() }}'s Turn",
                style = MaterialTheme.typography.titleMedium,
                color = if (currentTurn == ChessPieceColor.WHITE) Color.DarkGray else Color.Black
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
                                ChessSquare(
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

            Spacer(modifier = Modifier.height(24.dp))
            
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
fun ChessSquare(
    isDark: Boolean,
    piece: ChessPiece?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) Color(0xFFB58863) else Color(0xFFF0D9B5))
            .clickable { onClick() },
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
            Text(
                text = getPieceUnicode(piece),
                fontSize = 32.sp,
                color = if (piece.color == ChessPieceColor.WHITE) Color.White else Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun getPieceUnicode(piece: ChessPiece): String {
    return when (piece.color) {
        ChessPieceColor.WHITE -> when (piece.type) {
            ChessPieceType.PAWN -> "♙"
            ChessPieceType.ROOK -> "♖"
            ChessPieceType.KNIGHT -> "♘"
            ChessPieceType.BISHOP -> "♗"
            ChessPieceType.QUEEN -> "♕"
            ChessPieceType.KING -> "♔"
        }
        ChessPieceColor.BLACK -> when (piece.type) {
            ChessPieceType.PAWN -> "♟"
            ChessPieceType.ROOK -> "♜"
            ChessPieceType.KNIGHT -> "♞"
            ChessPieceType.BISHOP -> "♝"
            ChessPieceType.QUEEN -> "♛"
            ChessPieceType.KING -> "♚"
        }
    }
}

fun initialChessBoard(): Map<Position, ChessPiece> {
    val board = mutableMapOf<Position, ChessPiece>()
    
    // Black pieces
    board[Position(0, 0)] = ChessPiece(ChessPieceType.ROOK, ChessPieceColor.BLACK)
    board[Position(0, 1)] = ChessPiece(ChessPieceType.KNIGHT, ChessPieceColor.BLACK)
    board[Position(0, 2)] = ChessPiece(ChessPieceType.BISHOP, ChessPieceColor.BLACK)
    board[Position(0, 3)] = ChessPiece(ChessPieceType.QUEEN, ChessPieceColor.BLACK)
    board[Position(0, 4)] = ChessPiece(ChessPieceType.KING, ChessPieceColor.BLACK)
    board[Position(0, 5)] = ChessPiece(ChessPieceType.BISHOP, ChessPieceColor.BLACK)
    board[Position(0, 6)] = ChessPiece(ChessPieceType.KNIGHT, ChessPieceColor.BLACK)
    board[Position(0, 7)] = ChessPiece(ChessPieceType.ROOK, ChessPieceColor.BLACK)
    for (i in 0..7) board[Position(1, i)] = ChessPiece(ChessPieceType.PAWN, ChessPieceColor.BLACK)

    // White pieces
    board[Position(7, 0)] = ChessPiece(ChessPieceType.ROOK, ChessPieceColor.WHITE)
    board[Position(7, 1)] = ChessPiece(ChessPieceType.KNIGHT, ChessPieceColor.WHITE)
    board[Position(7, 2)] = ChessPiece(ChessPieceType.BISHOP, ChessPieceColor.WHITE)
    board[Position(7, 3)] = ChessPiece(ChessPieceType.QUEEN, ChessPieceColor.WHITE)
    board[Position(7, 4)] = ChessPiece(ChessPieceType.KING, ChessPieceColor.WHITE)
    board[Position(7, 5)] = ChessPiece(ChessPieceType.BISHOP, ChessPieceColor.WHITE)
    board[Position(7, 6)] = ChessPiece(ChessPieceType.KNIGHT, ChessPieceColor.WHITE)
    board[Position(7, 7)] = ChessPiece(ChessPieceType.ROOK, ChessPieceColor.WHITE)
    for (i in 0..7) board[Position(6, i)] = ChessPiece(ChessPieceType.PAWN, ChessPieceColor.WHITE)

    return board
}

@Preview(showBackground = true)
@Composable
fun ChessPagePreview() {
    MyApplicationTheme {
        ChessPage(onNavigateBack = {})
    }
}
