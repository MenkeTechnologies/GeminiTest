package com.example.myapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SnakeViewModelTest {

    private lateinit var viewModel: SnakeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SnakeViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() {
        assertEquals(20, viewModel.gridSize)
        assertEquals(3, viewModel.snake.size)
        assertEquals(Direction.RIGHT, viewModel.direction)
        assertFalse(viewModel.isGameOver)
        assertEquals(0, viewModel.score)
    }

    @Test
    fun testUpdateDirection() {
        viewModel.updateDirection(Direction.UP)
        assertEquals(Direction.UP, viewModel.direction)

        // Should not update to opposite direction
        viewModel.updateDirection(Direction.DOWN)
        assertEquals(Direction.UP, viewModel.direction)
    }

    @Test
    fun testResetGame() {
        viewModel.updateDirection(Direction.UP)
        viewModel.resetGame()
        assertEquals(Direction.RIGHT, viewModel.direction)
        assertEquals(3, viewModel.snake.size)
        assertFalse(viewModel.isGameOver)
        assertEquals(0, viewModel.score)
    }
}
