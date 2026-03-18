package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wealth_records")
data class WealthRecord(
    @PrimaryKey val id: Int = 1,
    val balance: Double = 0.0
)
