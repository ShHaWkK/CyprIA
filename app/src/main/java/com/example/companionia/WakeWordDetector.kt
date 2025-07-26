package com.example.companionia

import android.content.Context

/**
 * Simple wrapper around the Porcupine wake word engine.
 * In a real implementation this would load the Porcupine library
 * and start an audio stream to detect the custom keyword.
 * Here we only expose a callback to be triggered manually for now.
 */
class WakeWordDetector(private val context: Context, private val onWake: () -> Unit) {
    fun start() {
        // TODO integrate Porcupine keyword detection
    }

    fun stop() {
        // TODO stop keyword detection
    }

    /**
     * Temporary helper used while Porcupine is not integrated.
     * Call this when the wake word is detected externally.
     */
    fun trigger() {
        onWake()
    }
}
