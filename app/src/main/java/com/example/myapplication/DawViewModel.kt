package com.example.myapplication

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sin

class DawViewModel : ViewModel() {
    var isPlaying by mutableStateOf(false)
        private set

    val trackVolumes = mutableStateListOf(0.5f, 0.4f, 0.3f, 0.6f)
    val trackNames = listOf("NEON_LEAD", "CYBER_BASS", "GLITCH_BEAT", "ATMOS_VOX")
    private val frequencies = listOf(440.0, 110.0, 880.0, 220.0)

    fun togglePlayback() {
        isPlaying = !isPlaying
        if (isPlaying) {
            startAudioEngine()
        }
    }

    fun updateVolume(index: Int, volume: Float) {
        if (index in trackVolumes.indices) {
            trackVolumes[index] = volume
        }
    }

    private fun startAudioEngine() {
        viewModelScope.launch {
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

                try {
                    while (isActive && isPlaying) {
                        for (i in buffer.indices) {
                            var sample = 0.0
                            for (t in frequencies.indices) {
                                val vol = trackVolumes[t]
                                val freq = frequencies[t]
                                sample += vol * sin(2.0 * Math.PI * angle * freq / sampleRate)
                            }
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
}
