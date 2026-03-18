package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class KanbanViewModel(application: Application) : AndroidViewModel(application) {
    private val kanbanDao = AppDatabase.getDatabase(application).kanbanDao()
    val allTasks: Flow<List<KanbanTask>> = kanbanDao.getAllTasks()

    fun addTask(title: String) {
        viewModelScope.launch {
            kanbanDao.insertTask(KanbanTask(title = title))
        }
    }

    fun updateTask(task: KanbanTask) {
        viewModelScope.launch {
            kanbanDao.updateTask(task)
        }
    }

    fun deleteTask(task: KanbanTask) {
        viewModelScope.launch {
            kanbanDao.deleteTask(task)
        }
    }

    fun moveTask(task: KanbanTask, newStatus: KanbanStatus) {
        viewModelScope.launch {
            kanbanDao.updateTask(task.copy(status = newStatus))
        }
    }
}
