package com.example.companionia

import android.content.Context
import org.json.JSONObject
import java.io.File

object PermissionManager {
    private var config = JSONObject()

    fun load(context: Context) {
        val file = File(context.filesDir, "ia_config.json")
        if (file.exists()) {
            config = JSONObject(file.readText())
        }
    }

    fun isAppAllowed(packageName: String, action: String): Boolean {
        val apps = config.optJSONObject("apps") ?: return false
        val app = apps.optJSONObject(packageName) ?: return false
        return app.optBoolean(action, false)
    }
}
