package com.example.myapplication

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetUser() = runBlocking {
        val user = User(username = "neo", passwordHash = "hashed_password")
        userDao.insertUser(user)
        val fetchedUser = userDao.getUserByUsername("neo")
        assertEquals(user, fetchedUser)
    }

    @Test
    @Throws(Exception::class)
    fun getNonExistentUserReturnsNull() = runBlocking {
        val fetchedUser = userDao.getUserByUsername("trinity")
        assertNull(fetchedUser)
    }

    @Test
    @Throws(Exception::class)
    fun insertDuplicateUsernameReplaces() = runBlocking {
        val user1 = User(username = "neo", passwordHash = "pass1")
        val user2 = User(username = "neo", passwordHash = "pass2")
        userDao.insertUser(user1)
        userDao.insertUser(user2)
        val fetchedUser = userDao.getUserByUsername("neo")
        assertEquals("pass2", fetchedUser?.passwordHash)
    }
}
