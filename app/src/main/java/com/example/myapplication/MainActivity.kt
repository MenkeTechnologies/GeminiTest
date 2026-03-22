package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(true) }
            
            MyApplicationTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            })
                        }
                        composable("home") {
                            Greeting(
                                name = "Android",
                                isDarkMode = isDarkMode,
                                onDarkModeChange = { isDarkMode = it },
                                onNavigateToTommy = { navController.navigate("tommy") },
                                onNavigateToBob = { navController.navigate("bob") },
                                onNavigateToTest = { navController.navigate("test") },
                                onNavigateToChess = { navController.navigate("chess") },
                                onNavigateToTodoDetails = { navController.navigate("todo_details") },
                                onNavigateToDaw = { navController.navigate("daw") },
                                onNavigateToSnake = { navController.navigate("snake") },
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("tommy") {
                            TommyScreen(onNavigateBack = { navController.popBackStack() })
                        }
                        composable("bob") {
                            BobScreen(onNavigateBack = { navController.popBackStack() })
                        }
                        composable("test") {
                            TestPage(onNavigateBack = { navController.popBackStack() })
                        }
                        composable("chess") {
                            ChessPage(onNavigateBack = { navController.popBackStack() })
                        }
                        composable("todo_details") {
                            TodoDetailsPage(onNavigateBack = { navController.popBackStack() })
                        }
                        composable("daw") {
                            DawScreen(onNavigateBack = { navController.popBackStack() })
                        }
                        composable("snake") {
                            SnakeScreen(onNavigateBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CyberpunkBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFFF00FF), Color(0xFF00FFFF))
                )
            )
            .border(
                width = 2.dp,
                color = Color.Yellow,
                shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "NEO-DASHBOARD v2.0",
                color = Color.Black,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 4.sp
            )
            Text(
                text = "SYSTEM STATUS: OPERATIONAL",
                color = Color.Black.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelLarge,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Glitch effect decorative lines
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Yellow.copy(alpha = 0.5f))
                .align(Alignment.TopCenter)
                .padding(top = 10.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Yellow.copy(alpha = 0.5f))
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        )
    }
}

@Composable
fun FerrisWheel(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "FerrisWheelRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing)
        ),
        label = "Rotation"
    )

    val mainColor = Color(0xFF00FFFF) // Neon Cyan
    val cabinColor = Color(0xFFFF00FF) // Neon Magenta

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2 * 0.8f
        
        // Draw main structure
        drawCircle(
            color = mainColor,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
        
        // Draw spokes and cabins
        rotate(rotation, pivot = center) {
            for (i in 0 until 8) {
                val angle = (i * 45f) * (Math.PI / 180f).toFloat()
                val stopX = center.x + radius * cos(angle)
                val stopY = center.y + radius * sin(angle)
                
                drawLine(
                    color = mainColor,
                    start = center,
                    end = Offset(stopX, stopY),
                    strokeWidth = 1.dp.toPx()
                )
                
                // Draw cabin (staying upright)
                rotate(-rotation - (i * 45f), pivot = Offset(stopX, stopY)) {
                    drawRect(
                        color = cabinColor,
                        topLeft = Offset(stopX - 5.dp.toPx(), stopY - 5.dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(10.dp.toPx(), 10.dp.toPx())
                    )
                }
            }
        }
        
        // Support structure
        drawLine(
            color = mainColor,
            start = center,
            end = Offset(center.x - radius, size.height),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = mainColor,
            start = center,
            end = Offset(center.x + radius, size.height),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@Composable
fun HomeTile(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF1A1A1A), // Dark Cyberpunk Grey
    content: @Composable () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1.3f)
            .border(1.dp, Color(0xFF00FFFF).copy(alpha = 0.3f), CutCornerShape(bottomEnd = 8.dp)),
        shape = CutCornerShape(bottomEnd = 8.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF00FFFF), // Cyan text
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                fontFamily = FontFamily.Monospace,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    }
}

@Composable
fun Greeting(
    name: String,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onNavigateToTommy: () -> Unit,
    onNavigateToBob: () -> Unit,
    onNavigateToTest: () -> Unit,
    onNavigateToChess: () -> Unit,
    onNavigateToTodoDetails: () -> Unit,
    onNavigateToDaw: () -> Unit,
    onNavigateToSnake: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel = viewModel(),
    wealthViewModel: WealthViewModel = viewModel()
) {
    val todos by todoViewModel.allTodos.collectAsState(initial = emptyList())
    val balance by wealthViewModel.currentBalance.collectAsState(initial = 0.0)
    var showStockPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showStockPopup) {
        AlertDialog(
            onDismissRequest = { showStockPopup = false },
            containerColor = Color(0xFF121212),
            title = { 
                Text(
                    "STOCK MARKET SYNC", 
                    color = Color.Yellow, 
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                ) 
            },
            text = {
                Column {
                    Text("STONKS ARE UP! 🚀", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("GMNI: +420.69%", color = Color(0xFF00FF00))
                    Text("TMY: +100.00%", color = Color(0xFF00FF00))
                    Text("TODO: +12.5%", color = Color(0xFF00FF00))
                }
            },
            confirmButton = {
                TextButton(onClick = { showStockPopup = false }) {
                    Text("ACKNOWLEDGE", color = Color(0xFF00FFFF))
                }
            }
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black) // True Cyberpunk black
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Cyberpunk Banner
        item(span = { GridItemSpan(2) }) {
            CyberpunkBanner()
        }

        // Dashboard Heading
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "> INITIALIZING DASHBOARD_V2.0",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF00FFFF),
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        item {
            HomeTile("BALANCE", onClick = {}) {
                Text(
                    text = NumberFormat.getCurrencyInstance(Locale.US).format(balance),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF00FF00), // Matrix green
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            HomeTile("RELAX", onClick = {}) {
                FerrisWheel(modifier = Modifier.size(60.dp))
            }
        }

        item { HomeTile("TOMMY_OS", onClick = onNavigateToTommy) }
        item { HomeTile("BOB_CORE", onClick = onNavigateToBob) }
        item { HomeTile("TEST_LAB", onClick = onNavigateToTest) }
        item { HomeTile("CHESS_NET", onClick = onNavigateToChess) }
        
        item {
            HomeTile("GAME_INIT", onClick = onNavigateToSnake) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Gamepad,
                        contentDescription = null,
                        tint = Color(0xFF00FF00), // Neon Green
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "SNAKE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        item {
            HomeTile("TERMINAL", onClick = {
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_LAUNCHER)
                    setPackage("com.android.terminal")
                }
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Fallback or log if terminal app not found
                }
            }) {
                Text(
                    text = ">_",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF00FFFF),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            HomeTile("SHOWER_SYNC", onClick = {}) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "|||",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFF00FFFF),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "CLEANSE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        item {
            HomeTile("AUDIO_ENG", onClick = onNavigateToDaw) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = Color(0xFFFF00FF), // Neon Magenta
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "PRODUCE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        item {
            HomeTile("DEV_CORE", onClick = {}) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Code,
                        contentDescription = null,
                        tint = Color(0xFFFFFF00), // Neon Yellow
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "COMPILE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
        
        item {
            HomeTile("DARK_MODE", onClick = { onDarkModeChange(!isDarkMode) }) {
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeChange,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        
        item {
            HomeTile(
                "WEALTH_GEN",
                onClick = {
                    wealthViewModel.addAMillion()
                    showStockPopup = true
                },
                containerColor = Color(0xFF1A1A1A)
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Yellow,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        item {
            HomeTile("TASK_SYNC", onClick = onNavigateToTodoDetails) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.List, 
                        contentDescription = null, 
                        tint = Color(0xFFFF00FF), // Magenta
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        "${todos.count { !it.isDone }} PENDING", 
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        item {
            HomeTile("QUICK_ADD", onClick = onNavigateToTodoDetails) {
                Icon(
                    Icons.Default.Add, 
                    contentDescription = "Add task", 
                    tint = Color(0xFF00FFFF), 
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // Logout
        item(span = { GridItemSpan(2) }) {
            OutlinedButton(
                onClick = onLogout,
                shape = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .border(1.dp, Color(0xFFFF0000), CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
            ) {
                Text("TERMINATE SESSION", color = Color(0xFFFF0000), fontFamily = FontFamily.Monospace)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting(
            name = "Android",
            isDarkMode = true,
            onDarkModeChange = {},
            onNavigateToTommy = {},
            onNavigateToBob = {},
            onNavigateToTest = {},
            onNavigateToChess = {},
            onNavigateToTodoDetails = {},
            onNavigateToDaw = {},
            onNavigateToSnake = {},
            onLogout = {}
        )
    }
}
