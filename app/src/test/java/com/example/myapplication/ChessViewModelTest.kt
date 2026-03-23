package com.example.myapplication

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ChessViewModelTest {

    private lateinit var viewModel: ChessViewModel

    @Before
    fun setup() {
        viewModel = ChessViewModel()
    }

    @Test
    fun testInitialState() {
        assertEquals(32, viewModel.board.size)
        assertEquals(ChessPieceColor.WHITE, viewModel.currentTurn)
        assertNull(viewModel.selectedPosition)
    }

    @Test
    fun testSelectPiece() {
        val whitePiecePos = Position(7, 0) // Should have a white rook
        viewModel.handleSquareClick(whitePiecePos)
        assertEquals(whitePiecePos, viewModel.selectedPosition)
    }

    @Test
    fun testDeselectPiece() {
        val whitePiecePos = Position(7, 0)
        viewModel.handleSquareClick(whitePiecePos)
        viewModel.handleSquareClick(whitePiecePos)
        assertNull(viewModel.selectedPosition)
    }

    @Test
    fun testMovePiece() {
        val from = Position(6, 0) // White pawn
        val to = Position(5, 0)
        
        viewModel.handleSquareClick(from)
        viewModel.handleSquareClick(to)
        
        assertNull(viewModel.board[from])
        assertNotNull(viewModel.board[to])
        assertEquals(ChessPieceColor.BLACK, viewModel.currentTurn)
    }

    @Test
    fun testResetGame() {
        val from = Position(6, 0)
        val to = Position(5, 0)
        viewModel.handleSquareClick(from)
        viewModel.handleSquareClick(to)
        
        viewModel.resetGame()
        assertEquals(32, viewModel.board.size)
        assertEquals(ChessPieceColor.WHITE, viewModel.currentTurn)
        assertNull(viewModel.selectedPosition)
    }
}
