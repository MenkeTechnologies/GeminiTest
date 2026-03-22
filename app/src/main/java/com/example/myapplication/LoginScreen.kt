package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    val neonCyan = Color(0xFF00FFFF)
    val neonMagenta = Color(0xFFFF00FF)
    val neonYellow = Color(0xFFFFFF00)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Decorative background elements
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .border(1.dp, neonCyan.copy(alpha = 0.1f), CutCornerShape(topStart = 40.dp, bottomEnd = 40.dp))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cool Cyberpunk Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .clip(CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(neonMagenta, neonCyan)
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isLogin) "ACCESS_GRANTED" else "NEW_USER_INIT",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )
            }

            Text(
                text = if (isLogin) "> IDENTIFY_SUBJECT" else "> INITIALIZE_PROTOCOL",
                color = neonCyan,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("ID_USERNAME", fontFamily = FontFamily.Monospace) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = neonCyan) },
                shape = CutCornerShape(bottomEnd = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = neonCyan,
                    unfocusedBorderColor = neonCyan.copy(alpha = 0.5f),
                    focusedLabelColor = neonCyan,
                    unfocusedLabelColor = neonCyan.copy(alpha = 0.5f),
                    cursorColor = neonCyan,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("CODE_ENCRYPTION", fontFamily = FontFamily.Monospace) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = neonMagenta) },
                shape = CutCornerShape(bottomEnd = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = neonMagenta,
                    unfocusedBorderColor = neonMagenta.copy(alpha = 0.5f),
                    focusedLabelColor = neonMagenta,
                    unfocusedLabelColor = neonMagenta.copy(alpha = 0.5f),
                    cursorColor = neonMagenta,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = "!! ERROR: $errorMessage !!",
                    color = Color.Red,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    scope.launch {
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
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(2.dp, neonYellow, CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = neonYellow
                ),
                shape = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp)
            ) {
                Text(
                    text = if (isLogin) "ESTABLISH_UPLINK" else "CREATE_IDENTITY",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { isLogin = !isLogin },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isLogin) "> SWITCH_TO_REGISTRATION" else "> RETURN_TO_AUTH",
                    color = neonCyan.copy(alpha = 0.7f),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        
        // Bottom decorative line
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(4.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, neonCyan, neonMagenta, Color.Transparent)
                    )
                )
        )
    }
}
