package com.example.myapplication

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DawScreen(onNavigateBack: () -> Unit) {
    val neonCyan = Color(0xFF00FFFF)
    val neonMagenta = Color(0xFFFF00FF)
    val neonGreen = Color(0xFF00FF00)

    var isPlaying by remember { mutableStateOf(false) }
    val trackVolumes = remember { mutableStateListOf(0.5f, 0.4f, 0.3f, 0.6f) }
    val trackNames = listOf("NEON_LEAD", "CYBER_BASS", "GLITCH_BEAT", "ATMOS_VOX")
    val scope = rememberCoroutineScope()

    // Synth Engine Logic
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            withContext(Dispatchers.Default) {
                val sampleRate = 44100
                val minBufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                
                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack.play()
                
                val buffer = ShortArray(minBufferSize)
                var angle = 0.0
                val frequencies = listOf(440.0, 110.0, 880.0, 220.0) // A4, A2, A5, A3

                try {
                    while (isActive && isPlaying) {
                        for (i in buffer.indices) {
                            var sample = 0.0
                            // Mix tracks
                            for (t in frequencies.indices) {
                                val vol = trackVolumes[t]
                                val freq = frequencies[t]
                                sample += vol * sin(2.0 * Math.PI * angle * freq / sampleRate)
                            }
                            // Hard clipping / limiting
                            sample = sample.coerceIn(-1.0, 1.0)
                            buffer[i] = (sample * Short.MAX_VALUE).toInt().toShort()
                            angle += 1.0
                        }
                        audioTrack.write(buffer, 0, buffer.size)
                    }
                } finally {
                    audioTrack.stop()
                    audioTrack.release()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "NEO_DAW_v1.0.sh",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = neonCyan
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = neonCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Transport Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, neonCyan.copy(alpha = 0.5f), CutCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isPlaying = true }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = if (isPlaying) neonGreen else Color.Gray)
                }
                IconButton(onClick = { isPlaying = false }) {
                    Icon(Icons.Default.Stop, contentDescription = "Stop", tint = if (!isPlaying) neonMagenta else Color.Gray)
                }
                Text("BPM: 128", color = neonCyan, fontFamily = FontFamily.Monospace)
                
                if (isPlaying) {
                    Text("ONLINE", color = neonGreen, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                } else {
                    Text("STANDBY", color = Color.Red, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("> INITIALIZING_TRACK_STACK", color = neonGreen, fontFamily = FontFamily.Monospace, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(8.dp))

            // Track List
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(trackNames) { index, name ->
                    HackerTrackItem(
                        name = name, 
                        volume = trackVolumes[index],
                        onVolumeChange = { trackVolumes[index] = it },
                        neonCyan = neonCyan, 
                        neonMagenta = neonMagenta,
                        isPlaying = isPlaying
                    )
                }
            }
            
            if (isPlaying) {
                Spacer(modifier = Modifier.weight(1f))
                SpectrumAnalyzer(neonCyan)
            }
        }
    }
}

@Composable
fun HackerTrackItem(
    name: String, 
    volume: Float, 
    onVolumeChange: (Float) -> Unit,
    neonCyan: Color, 
    neonMagenta: Color,
    isPlaying: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, neonCyan.copy(alpha = 0.3f), CutCornerShape(topStart = 8.dp))
            .background(Color(0xFF0A0A0A))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(name, color = Color.White, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text("${(volume * 100).toInt()}%", color = neonCyan, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = volume,
                onValueChange = onVolumeChange,
                colors = SliderDefaults.colors(
                    thumbColor = neonCyan,
                    activeTrackColor = neonCyan,
                    inactiveTrackColor = Color.DarkGray
                ),
                modifier = Modifier.height(24.dp)
            )
            
            if (isPlaying) {
                TrackVisualizer(neonCyan, volume)
            }
        }
    }
}

@Composable
fun TrackVisualizer(color: Color, volume: Float) {
    val infiniteTransition = rememberInfiniteTransition()
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.fillMaxWidth().height(10.dp)) {
        val bars = 30
        val barWidth = size.width / bars
        for (i in 0 until bars) {
            val randomFactor = (sin(i.toFloat() + phase * 10f) + 1f) / 2f
            val barHeight = size.height * randomFactor * volume
            drawRect(
                color = color.copy(alpha = 0.5f),
                topLeft = Offset(i * barWidth, size.height - barHeight),
                size = Size(barWidth - 2.dp.toPx(), barHeight)
            )
        }
    }
}

@Composable
fun SpectrumAnalyzer(color: Color) {
    val infiniteTransition = rememberInfiniteTransition()
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        )
    )

    Canvas(modifier = Modifier.fillMaxWidth().height(60.dp).border(1.dp, color.copy(alpha = 0.2f))) {
        val points = 50
        val step = size.width / points
        for (i in 0 until points) {
            val h = (sin(i.toFloat() * 0.5f + phase * 20f) + 1f) / 2f * size.height
            drawRect(
                color = color,
                topLeft = Offset(i * step, size.height - h),
                size = Size(step / 2, h)
            )
        }
    }
}
