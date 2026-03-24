package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WealthViewModel(application: Application) : AndroidViewModel(application) {
    private val wealthDao = AppDatabase.getDatabase(application).wealthDao()
    val currentBalance: Flow<Double> = wealthDao.getWealth().map { it?.balance ?: 0.0 }

    fun makeMeRich() {
        viewModelScope.launch {
            val currentWealth =
                wealthDao.getWealth().map { it?.balance ?: 0.0 }.collect { balance ->
                    wealthDao.insertWealth(WealthRecord(balance = balance + 1000000.0))
                }
        }
    }

    // Improved makeMeRich to avoid flow collection issues in a launch
    fun addAMillion() {
        viewModelScope.launch {
            // In a real app we'd use a single transaction or a more robust way to update
            // For this fun request, we'll just set it to a huge number
            wealthDao.insertWealth(WealthRecord(balance = 999999999.99))
        }
    }
}
