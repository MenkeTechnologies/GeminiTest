package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.abs

class CheckersViewModel : ViewModel() {
    var board by mutableStateOf(initialBoard())
        private set
    var selectedPosition by mutableStateOf<Position?>(null)
        private set
    var currentTurn by mutableStateOf(PieceType.BLACK)
        private set
    var message by mutableStateOf("Black's Turn")
        private set

    fun handleSquareClick(pos: Position) {
        val pieceAtPos = board[pos]

        if (selectedPosition == null) {
            // Select a piece
            if (pieceAtPos != null && pieceAtPos.type == currentTurn) {
                selectedPosition = pos
            }
        } else {
            val from = selectedPosition!!
            val piece = board[from]!!

            if (pos == from) {
                selectedPosition = null
                return
            }

            // Simple movement logic
            val rowDiff = pos.row - from.row
            val colDiff = pos.col - from.col
            val isForward = if (piece.type == PieceType.BLACK) rowDiff < 0 else rowDiff > 0
            val canMoveAnywhere = piece.isKing

            // Check if destination is empty and diagonal
            if (board[pos] == null && abs(colDiff) == abs(rowDiff)) {

                // Normal move (1 square)
                if (abs(rowDiff) == 1 && (isForward || canMoveAnywhere)) {
                    val newBoard = board.toMutableMap()
                    newBoard.remove(from)
                    var updatedPiece = piece
                    if ((piece.type == PieceType.BLACK && pos.row == 0) || (piece.type == PieceType.RED && pos.row == 7)) {
                        updatedPiece = piece.copy(isKing = true)
                    }
                    newBoard[pos] = updatedPiece
                    board = newBoard
                    currentTurn = if (currentTurn == PieceType.BLACK) PieceType.RED else PieceType.BLACK
                    selectedPosition = null
                    message = "${currentTurn.name.lowercase().replaceFirstChar { it.uppercase() }}'s Turn"
                }
                // Jump (2 squares)
                else if (abs(rowDiff) == 2 && (isForward || canMoveAnywhere)) {
                    val midPos = Position((from.row + pos.row) / 2, (from.col + pos.col) / 2)
                    val midPiece = board[midPos]
                    if (midPiece != null && midPiece.type != piece.type) {
                        val newBoard = board.toMutableMap()
                        newBoard.remove(from)
                        newBoard.remove(midPos)
                        var updatedPiece = piece
                        if ((piece.type == PieceType.BLACK && pos.row == 0) || (piece.type == PieceType.RED && pos.row == 7)) {
                            updatedPiece = piece.copy(isKing = true)
                        }
                        newBoard[pos] = updatedPiece
                        board = newBoard
                        currentTurn = if (currentTurn == PieceType.BLACK) PieceType.RED else PieceType.BLACK
                        selectedPosition = null
                        message = "${currentTurn.name.lowercase().replaceFirstChar { it.uppercase() }}'s Turn"
                    }
                }
            } else if (pieceAtPos != null && pieceAtPos.type == currentTurn) {
                // Change selection
                selectedPosition = pos
            }
        }
    }

    fun resetGame() {
        board = initialBoard()
        selectedPosition = null
        currentTurn = PieceType.BLACK
        message = "Black's Turn"
    }
}
