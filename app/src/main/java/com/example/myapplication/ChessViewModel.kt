package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ChessViewModel : ViewModel() {
    var board by mutableStateOf(initialChessBoard())
        private set
    var selectedPosition by mutableStateOf<Position?>(null)
        private set
    var currentTurn by mutableStateOf(ChessPieceColor.WHITE)
        private set

    fun handleSquareClick(pos: Position) {
        val pieceAtPos = board[pos]

        if (selectedPosition == null) {
            if (pieceAtPos != null && pieceAtPos.color == currentTurn) {
                selectedPosition = pos
            }
        } else {
            val from = selectedPosition!!
            if (from == pos) {
                selectedPosition = null
                return
            }

            // Simple movement (no full chess rules for now, just moving)
            val newBoard = board.toMutableMap()
            newBoard[pos] = board[from]!!
            newBoard.remove(from)
            board = newBoard
            currentTurn =
                if (currentTurn == ChessPieceColor.WHITE) ChessPieceColor.BLACK else ChessPieceColor.WHITE
            selectedPosition = null
        }
    }

    fun resetGame() {
        board = initialChessBoard()
        selectedPosition = null
        currentTurn = ChessPieceColor.WHITE
    }
}
