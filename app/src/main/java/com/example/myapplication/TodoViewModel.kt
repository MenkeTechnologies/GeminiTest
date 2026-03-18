package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao = AppDatabase.getDatabase(application).todoDao()
    val allTodos: Flow<List<Todo>> = todoDao.getAllTodos()

    fun addTodo(task: String) {
        viewModelScope.launch {
            todoDao.insertTodo(Todo(task = task))
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.deleteTodo(todo)
        }
    }
}
