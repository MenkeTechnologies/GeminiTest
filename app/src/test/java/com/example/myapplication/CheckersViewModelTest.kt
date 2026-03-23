package com.example.myapplication

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CheckersViewModelTest {

    private lateinit var viewModel: CheckersViewModel

    @Before
    fun setup() {
        viewModel = CheckersViewModel()
    }

    @Test
    fun testInitialState() {
        assertEquals(24, viewModel.board.size) // 12 red, 12 black
        assertEquals(PieceType.BLACK, viewModel.currentTurn)
        assertEquals("Black's Turn", viewModel.message)
        assertNull(viewModel.selectedPosition)
    }

    @Test
    fun testSelectPiece() {
        val blackPiecePos = Position(5, 0) // Should have a black piece
        viewModel.handleSquareClick(blackPiecePos)
        assertEquals(blackPiecePos, viewModel.selectedPosition)
    }

    @Test
    fun testDeselectPiece() {
        val blackPiecePos = Position(5, 0)
        viewModel.handleSquareClick(blackPiecePos)
        viewModel.handleSquareClick(blackPiecePos)
        assertNull(viewModel.selectedPosition)
    }

    @Test
    fun testMovePiece() {
        val from = Position(5, 0)
        val to = Position(4, 1)
        
        viewModel.handleSquareClick(from)
        viewModel.handleSquareClick(to)
        
        assertNull(viewModel.board[from])
        assertNotNull(viewModel.board[to])
        assertEquals(PieceType.RED, viewModel.currentTurn)
    }

    @Test
    fun testResetGame() {
        val from = Position(5, 0)
        val to = Position(4, 1)
        viewModel.handleSquareClick(from)
        viewModel.handleSquareClick(to)
        
        viewModel.resetGame()
        assertEquals(24, viewModel.board.size)
        assertEquals(PieceType.BLACK, viewModel.currentTurn)
        assertNull(viewModel.selectedPosition)
    }
}
