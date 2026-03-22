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
class WealthDaoTest {

    private lateinit var wealthDao: WealthDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        wealthDao = db.wealthDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWealth() = runBlocking {
        val record = WealthRecord(balance = 5000.0)
        wealthDao.insertWealth(record)
        val fetchedWealth = wealthDao.getWealth().first()
        assertEquals(5000.0, fetchedWealth?.balance ?: 0.0, 0.1)
    }

    @Test
    @Throws(Exception::class)
    fun updateWealth() = runBlocking {
        val record = WealthRecord(balance = 1000.0)
        wealthDao.insertWealth(record)
        
        val updatedRecord = record.copy(balance = 2000.0)
        wealthDao.updateWealth(updatedRecord)
        
        val fetchedWealth = wealthDao.getWealth().first()
        assertEquals(2000.0, fetchedWealth?.balance ?: 0.0, 0.1)
    }
}
