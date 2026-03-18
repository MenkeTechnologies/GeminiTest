package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class KanbanStatus {
    BACKLOG, IN_PROGRESS, DONE
}

@Entity(tableName = "kanban_tasks")
data class KanbanTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val status: KanbanStatus = KanbanStatus.BACKLOG
)
