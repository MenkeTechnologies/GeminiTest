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
class KanbanDaoTest {

    private lateinit var kanbanDao: KanbanDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        kanbanDao = db.kanbanDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTasks() = runBlocking {
        val task = KanbanTask(title = "Fix bug", status = KanbanStatus.BACKLOG)
        kanbanDao.insertTask(task)
        val allTasks = kanbanDao.getAllTasks().first()
        assertEquals(1, allTasks.size)
        assertEquals("Fix bug", allTasks[0].title)
    }

    @Test
    @Throws(Exception::class)
    fun updateTaskStatus() = runBlocking {
        val task = KanbanTask(id = 1, title = "Feature X", status = KanbanStatus.BACKLOG)
        kanbanDao.insertTask(task)
        
        val updatedTask = task.copy(status = KanbanStatus.IN_PROGRESS)
        kanbanDao.updateTask(updatedTask)
        
        val allTasks = kanbanDao.getAllTasks().first()
        assertEquals(KanbanStatus.IN_PROGRESS, allTasks[0].status)
    }

    @Test
    @Throws(Exception::class)
    fun deleteTask() = runBlocking {
        val task = KanbanTask(id = 1, title = "Temporary task")
        kanbanDao.insertTask(task)
        kanbanDao.deleteTask(task)
        val allTasks = kanbanDao.getAllTasks().first()
        assertEquals(0, allTasks.size)
    }
}
