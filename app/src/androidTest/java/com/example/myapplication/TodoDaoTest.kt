package com.example.myapplication

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TodoDaoTest {

    private lateinit var todoDao: TodoDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        todoDao = db.todoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTodo() = runBlocking {
        val todo = Todo(id = 1, task = "Test Task", isDone = false)
        todoDao.insertTodo(todo)
        val allTodos = todoDao.getAllTodos().first()
        assertEquals("Test Task", allTodos[0].task)
    }

    @Test
    @Throws(Exception::class)
    fun deleteTodo() = runBlocking {
        val todo = Todo(id = 1, task = "To be deleted", isDone = false)
        todoDao.insertTodo(todo)
        todoDao.deleteTodo(todo)
        val allTodos = todoDao.getAllTodos().first()
        assertEquals(0, allTodos.size)
    }

    @Test
    @Throws(Exception::class)
    fun updateTodo() = runBlocking {
        val todo = Todo(id = 1, task = "Initial Task", isDone = false)
        todoDao.insertTodo(todo)
        val updatedTodo = todo.copy(isDone = true)
        todoDao.updateTodo(updatedTodo)
        val allTodos = todoDao.getAllTodos().first()
        assertEquals(true, allTodos[0].isDone)
    }
}
