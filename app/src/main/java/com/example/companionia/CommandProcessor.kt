package com.example.companionia

import android.content.Context
import android.content.Intent
import android.provider.Settings

/**
 * Very small command processor to demonstrate how voice commands
 * could be executed once converted to text.
 * A real implementation would use a local LLM for understanding.
 */
class CommandProcessor(private val context: Context) {
    fun process(text: String) {
        val lower = text.lowercase()
        when {
            lower.startsWith("open ") -> {
                val pkg = lower.removePrefix("open ").trim()
                launchApp(pkg)
            }
            lower == "open settings" -> {
                val intent = Intent(Settings.ACTION_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            // Additional commands would go here
        }
    }

    private fun launchApp(packageName: String) {
        val pm = context.packageManager
        val intent = pm.getLaunchIntentForPackage(packageName)
        if (intent != null && PermissionManager.isAppAllowed(packageName, "control")) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
