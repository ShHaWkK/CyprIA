package com.example.companionia

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.speech.SpeechRecognizer
import android.speech.RecognitionListener
import android.os.Bundle

class CompanionService : Service() {
    private lateinit var recognizer: SpeechRecognizer
    private lateinit var wakeWord: WakeWordDetector
    private lateinit var commandProcessor: CommandProcessor

    override fun onCreate() {
        super.onCreate()
        recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {}
            override fun onResults(results: Bundle?) {
                val texts = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                texts?.firstOrNull()?.let { commandProcessor.process(it) }
            }
        })
        commandProcessor = CommandProcessor(this)
        wakeWord = WakeWordDetector(this) { startStt() }
        PermissionManager.load(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startListening()
            ACTION_STOP -> stopListening()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startListening() {
        wakeWord.start()
    }

    private fun startStt() {
        // TODO replace SpeechRecognizer with Whisper.cpp for offline STT
        val intent = Intent().apply {
            putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form")
        }
        recognizer.startListening(intent)
    }

    private fun stopListening() {
        wakeWord.stop()
        recognizer.stopListening()
    }

    companion object {
        const val ACTION_START = "com.example.companionia.START"
        const val ACTION_STOP = "com.example.companionia.STOP"
    }
}
