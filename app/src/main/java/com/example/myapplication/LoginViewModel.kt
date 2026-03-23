package com.example.myapplication

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var isLogin by mutableStateOf(true)
    var errorMessage by mutableStateOf("")
        private set

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun toggleLoginMode() {
        isLogin = !isLogin
        errorMessage = ""
    }

    fun performAction(onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = db.userDao().getUserByUsername(username)
            if (isLogin) {
                if (user != null && user.passwordHash == password) {
                    onLoginSuccess()
                } else {
                    errorMessage = "CREDENTIAL_MISMATCH"
                }
            } else {
                if (user == null) {
                    db.userDao().insertUser(User(username, password))
                    onLoginSuccess()
                } else {
                    errorMessage = "SUBJECT_EXISTS"
                }
            }
        }
    }
}
