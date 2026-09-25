package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.model.SoundStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object SoundPlayer {
    private val scope = CoroutineScope(Dispatchers.Default)
    private const val SAMPLE_RATE = 44100

    fun playChargingSound(style: SoundStyle) {
        scope.launch {
            try {
                val samples = generateSamples(style)
                playPcm(samples)
            } catch (_: Exception) {
            }
        }
    }

    private fun generateSamples(style: SoundStyle): ShortArray {
        return when (style) {
            SoundStyle.SOFT -> generateSoftChime()
            SoundStyle.ELECTRIC -> generateElectricChirp()
            SoundStyle.FUTURISTIC -> generateFuturisticSynth()
            SoundStyle.ENERGY -> generateEnergySweep()
            SoundStyle.MINIMAL -> generateMinimalClick()
        }
    }

    private fun generateSoftChime(): ShortArray {
        val durationSec = 0.6f
        val numSamples = (durationSec * SAMPLE_RATE).toInt()
        val buffer = ShortArray(numSamples)
        val f1 = 523.25 // C5
        val f2 = 659.25 // E5
        val f3 = 783.99 // G5

        for (i in 0 until numSamples) {
            val t = i.toFloat() / SAMPLE_RATE
            val envelope = exp(-4.5 * t).toFloat()
            val sample = (0.5 * sin(2.0 * PI * f1 * t) +
                    0.3 * sin(2.0 * PI * f2 * t) +
                    0.2 * sin(2.0 * PI * f3 * t)) * envelope
            buffer[i] = (sample * 24000).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateElectricChirp(): ShortArray {
        val durationSec = 0.35f
        val numSamples = (durationSec * SAMPLE_RATE).toInt()
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toFloat() / SAMPLE_RATE
            val progress = t / durationSec
            val freq = 300.0 + 900.0 * (progress * progress)
            val envelope = sin(PI * progress).toFloat()
            val sample = sin(2.0 * PI * freq * t) * envelope
            // Add subtle grit
            val grit = if (sample > 0.6) 0.8 else if (sample < -0.6) -0.8 else sample
            buffer[i] = (grit * 22000).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateFuturisticSynth(): ShortArray {
        val durationSec = 0.5f
        val numSamples = (durationSec * SAMPLE_RATE).toInt()
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toFloat() / SAMPLE_RATE
            val progress = t / durationSec
            val freq1 = 440.0 + 440.0 * progress
            val freq2 = 880.0 + 220.0 * sin(progress * PI)
            val envelope = (1f - progress) * (if (progress < 0.05f) progress / 0.05f else 1f)
            val sample = (0.6 * sin(2.0 * PI * freq1 * t) + 0.4 * sin(2.0 * PI * freq2 * t)) * envelope
            buffer[i] = (sample * 26000).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateEnergySweep(): ShortArray {
        val durationSec = 0.45f
        val numSamples = (durationSec * SAMPLE_RATE).toInt()
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toFloat() / SAMPLE_RATE
            val progress = t / durationSec
            val freq = 140.0 + 520.0 * (progress * progress * progress)
            val envelope = (1f - progress * 0.7f) * (if (progress < 0.1f) progress / 0.1f else 1f)
            val sample = sin(2.0 * PI * freq * t) * envelope
            buffer[i] = (sample * 27000).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateMinimalClick(): ShortArray {
        val durationSec = 0.08f
        val numSamples = (durationSec * SAMPLE_RATE).toInt()
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toFloat() / SAMPLE_RATE
            val progress = t / durationSec
            val envelope = exp(-35.0 * progress).toFloat()
            val sample = sin(2.0 * PI * 1200.0 * t) * envelope
            buffer[i] = (sample * 20000).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun playPcm(buffer: ShortArray) {
        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(SAMPLE_RATE)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(buffer.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        try {
            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            Thread.sleep(700)
        } catch (_: Exception) {
        } finally {
            try {
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Exception) {
            }
        }
    }
}
