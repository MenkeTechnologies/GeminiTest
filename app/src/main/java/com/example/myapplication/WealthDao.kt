package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WealthDao {
    @Query("SELECT * FROM wealth_records WHERE id = 1")
    fun getWealth(): Flow<WealthRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWealth(wealth: WealthRecord)

    @Update
    suspend fun updateWealth(wealth: WealthRecord)
}
