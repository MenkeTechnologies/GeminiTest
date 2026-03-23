package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class Direction { UP, DOWN, LEFT, RIGHT }
data class SnakePoint(val x: Int, val y: Int)

class SnakeViewModel : ViewModel() {
    val gridSize = 20

    var snake by mutableStateOf(listOf(SnakePoint(5, 10), SnakePoint(4, 10), SnakePoint(3, 10)))
        private set
    var food by mutableStateOf(SnakePoint(15, 10))
        private set
    var direction by mutableStateOf(Direction.RIGHT)
        private set
    var isGameOver by mutableStateOf(false)
        private set
    var score by mutableStateOf(0)
        private set

    init {
        startGameLoop()
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (true) {
                if (!isGameOver) {
                    delay(150)
                    moveSnake()
                } else {
                    delay(500)
                }
            }
        }
    }

    private fun moveSnake() {
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

    fun updateDirection(newDirection: Direction) {
        val isOpposite = when (newDirection) {
            Direction.UP -> direction == Direction.DOWN
            Direction.DOWN -> direction == Direction.UP
            Direction.LEFT -> direction == Direction.RIGHT
            Direction.RIGHT -> direction == Direction.LEFT
        }
        if (!isOpposite) {
            direction = newDirection
        }
    }

    fun resetGame() {
        snake = listOf(SnakePoint(5, 10), SnakePoint(4, 10), SnakePoint(3, 10))
        direction = Direction.RIGHT
        isGameOver = false
        score = 0
        food = SnakePoint(Random.nextInt(gridSize), Random.nextInt(gridSize))
    }
}
