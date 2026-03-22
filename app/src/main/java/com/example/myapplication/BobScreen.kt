package com.example.myapplication

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun CircusTent(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(200.dp)) {
        val width = size.width
        val height = size.height
        
        // Roof
        val roofPath = Path().apply {
            moveTo(width / 2, 0f)
            lineTo(width, height * 0.4f)
            lineTo(0f, height * 0.4f)
            close()
        }
        drawPath(path = roofPath, color = Color.Red)
        
        // Stripes on roof
        for (i in 1 until 5) {
            val x = width * (i / 5f)
            drawLine(
                color = Color.White,
                start = Offset(width / 2, 0f),
                end = Offset(x, height * 0.4f),
                strokeWidth = 4.dp.toPx()
            )
        }

        // Main body
        drawRect(
            color = Color.Red,
            topLeft = Offset(0f, height * 0.4f),
            size = androidx.compose.ui.geometry.Size(width, height * 0.6f)
        )
        
        // Door
        val doorWidth = width * 0.3f
        val doorHeight = height * 0.4f
        drawRect(
            color = Color.Black,
            topLeft = Offset((width - doorWidth) / 2, height - doorHeight),
            size = androidx.compose.ui.geometry.Size(doorWidth, doorHeight)
        )
    }
}

@Composable
fun BouncingAnimal(imageUrl: String, delay: Int = 0) {
    val infiniteTransition = rememberInfiniteTransition(label = "BouncingAnimal")
    val yOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -50f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing, delayMillis = delay),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yOffset"
    )

    AsyncImage(
        model = imageUrl,
        contentDescription = "Circus Animal",
        modifier = Modifier
            .size(60.dp)
            .offset(y = yOffset.dp)
    )
}

@Composable
fun BobScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "🎪 Bob's Circus 🎪", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        CircusTent(modifier = Modifier.padding(16.dp))
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            BouncingAnimal(imageUrl = "https://placedog.net/100/100?id=1", delay = 0)
            BouncingAnimal(imageUrl = "https://placedog.net/100/100?id=2", delay = 200)
            BouncingAnimal(imageUrl = "https://placedog.net/100/100?id=3", delay = 400)
        }

        Spacer(modifier = Modifier.weight(1f))
        
        Button(onClick = onNavigateBack) {
            Text("Back to Home")
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BobScreenPreview() {
    MyApplicationTheme {
        BobScreen(onNavigateBack = {})
    }
}
