package com.example.companionia

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CompanionUI() }
    }
}

@Composable
fun CompanionUI() {
    val ctx = LocalContext.current
    var listening by remember { mutableStateOf(false) }
    var showPermissions by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (showPermissions) {
            Column(modifier = Modifier.fillMaxSize()) {
                PermissionScreen()
                Spacer(Modifier.height(16.dp))
                Button(onClick = { showPermissions = false }, modifier = Modifier.align(Alignment.End).padding(16.dp)) {
                    Text("Back")
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Companion IA", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val intent = Intent(ctx, CompanionService::class.java)
                    intent.action = if (listening) CompanionService.ACTION_STOP else CompanionService.ACTION_START
                    ctx.startService(intent)
                    listening = !listening
                }) {
                    Text(if (listening) "Stop" else "Start")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { showPermissions = true }) {
                    Text("Permissions")
                }
            }
        }
    }
}
