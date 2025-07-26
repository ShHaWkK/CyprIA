# Companion IA

Companion IA est un copilote Android 100 % local capable d'exécuter des
commandes vocales et de contrôler le téléphone sans connexion Internet.
L'objectif est de proposer une solution simple et respectueuse de la vie
privée.

## Fonctionnalités principales

- Activation vocale via un mot-clé personnalisé (ex. « Ok Companion »)
- Transcription offline grâce à Whisper.cpp
- Traitement des commandes par un modèle LLM embarqué
- Lecture d'écran et reconnaissance d'éléments via OCR et computer vision
- Contrôle des applications et du système (shell ou API Accessibilité)
- Gestion dynamique des permissions dans `ia_config.json`

## Arborescence

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

`MainActivity.kt` utilise Compose Material 3 pour afficher un `TopAppBar` et un
bouton flottant. Celui‑ci permet de démarrer ou d'arrêter l'assistant tandis que
l'écran des permissions reste accessible via un bouton :

```kotlin
Scaffold(
    floatingActionButton = {
        FloatingActionButton(onClick = {
            val intent = Intent(ctx, CompanionService::class.java)
            intent.action = if (listening) CompanionService.ACTION_STOP else CompanionService.ACTION_START
            ctx.startService(intent)
            listening = !listening
        }) {
            Icon(
                imageVector = if (listening) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = null
            )
        }
    }
) { /* ... */ }
```

`CompanionService.kt` gère la reconnaissance vocale locale. Un détecteur de mot
clé (`WakeWordDetector`) déclenche la transcription (à terme via Whisper.cpp).
Les commandes textuelles sont ensuite interprétées par `CommandProcessor`.

## Lancer le projet

1. Ouvrir le dossier dans Android Studio.
2. Laisser Gradle télécharger les dépendances (connexion initiale requise).
3. Compiler et exécuter sur un appareil Android (minSdk 26).
4. Appuyer sur le bouton « Start » pour activer l'assistant.

## Configuration des permissions

Le fichier `ia_config.json` permet de définir les applications ou fonctionnalités
que l'IA peut lire ou contrôler. Ce fichier est chargé au démarrage par
`PermissionManager`.
Un écran dédié dans l'application permet de modifier ces droits via des
cases à cocher pour chaque application.

## Licence

MIT
