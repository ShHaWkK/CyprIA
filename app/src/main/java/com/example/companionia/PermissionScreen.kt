package com.example.companionia

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.json.JSONObject
import java.io.File

@Composable
fun PermissionScreen() {
    val ctx = LocalContext.current
    val packages = remember { ctx.packageManager.getInstalledPackages(0) }
    val configState = remember { mutableStateOf(loadConfig(ctx)) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Permissions", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(packages) { pkgInfo ->
                val pkg = pkgInfo.packageName
                val read = remember { mutableStateOf(configState.value.isAllowed(pkg, "read")) }
                val control = remember { mutableStateOf(configState.value.isAllowed(pkg, "control")) }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(pkg, modifier = Modifier.weight(1f))
                    Row {
                        Checkbox(read.value, onCheckedChange = {
                            read.value = it
                            configState.value.set(pkg, "read", it)
                        })
                        Checkbox(control.value, onCheckedChange = {
                            control.value = it
                            configState.value.set(pkg, "control", it)
                        })
                    }
                }
            }
        }
        Button(onClick = { saveConfig(ctx, configState.value) }, modifier = Modifier.fillMaxWidth()) {
            Text("Save")
        }
    }
}

private class MutableConfig(private val json: JSONObject) {
    fun isAllowed(pkg: String, key: String): Boolean {
        val apps = json.optJSONObject("apps") ?: return false
        val app = apps.optJSONObject(pkg) ?: return false
        return app.optBoolean(key, false)
    }

    fun set(pkg: String, key: String, value: Boolean) {
        val apps = json.optJSONObject("apps") ?: run {
            json.put("apps", JSONObject())
            json.getJSONObject("apps")
        }
        val app = apps.optJSONObject(pkg) ?: run {
            val obj = JSONObject()
            apps.put(pkg, obj)
            obj
        }
        app.put(key, value)
    }

    fun toJson(): JSONObject = json
}

private fun loadConfig(context: Context): MutableConfig {
    val file = File(context.filesDir, "ia_config.json")
    val json = if (file.exists()) JSONObject(file.readText()) else JSONObject().apply { put("apps", JSONObject()) }
    return MutableConfig(json)
}

private fun saveConfig(context: Context, config: MutableConfig) {
    val file = File(context.filesDir, "ia_config.json")
    file.writeText(config.toJson().toString(2))
}
