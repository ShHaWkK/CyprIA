# Companion AI

Companion AI is a 100% local Android co-pilot capable of executing voice commands and controlling the phone without an Internet connection. The goal is to offer a simple and privacy-respecting solution.

## Main Features

- Voice activation via a custom wake word (e.g., "Ok Companion")  
- Offline transcription powered by Whisper.cpp  
- Command processing by an embedded LLM model  
- Screen reading and element recognition using OCR and computer vision  
- Application and system control (via shell or Accessibility API)  
- Dynamic permission management through `ia_config.json`  

## Project Structure

```
app/
  src/main/
    java/com/example/companionia/
      MainActivity.kt
      CompanionService.kt
      PermissionManager.kt
    res/layout/
      activity_main.xml
    AndroidManifest.xml
build.gradle
settings.gradle
ia_config.json
```

## Aperçu du code

`MainActivity.kt` provides a Compose interface to start the service and access the permissions management screen:

```kotlin
Button(onClick = {
    val intent = Intent(ctx, CompanionService::class.java)
    intent.action = if (listening) CompanionService.ACTION_STOP else CompanionService.ACTION_START
    ctx.startService(intent)
    listening = !listening
}) {
    Text(if (listening) "Stop" else "Start")
}
```

`CompanionService.kt` gère la reconnaissance vocale locale. Un détecteur de mot
clé (`WakeWordDetector`) déclenche la transcription (à terme via Whisper.cpp).
Les commandes textuelles sont ensuite interprétées par `CommandProcessor`.


`CompanionService.kt` handles local voice recognition. A wake word detector (`WakeWordDetector`) triggers transcription (eventually via Whisper.cpp). Text commands are then processed by `CommandProcessor`.

## How to Run the Project

1. Open the folder in Android Studio.  
2. Let Gradle download dependencies (initial internet connection required).  
3. Build and run on an Android device (minSdk 26).  
4. Press the "Start" button to activate the assistant.

## Permission Configuration

The `ia_config.json` file defines which apps or features the AI can read or control. This file is loaded at startup by `PermissionManager`.  
A dedicated screen in the app allows modifying these rights via checkboxes for each application.

## License
MIT
