<<<<<<< HEAD
# Dream-journal
An android app that take your dream notes and stores in database
=======
# Dream Journal (Compose + Room + WorkManager + MPAndroidChart)

A starter app to record dreams with on-device rule-based mood analysis and trends.

## Quickstart

1. Open the folder in Android Studio (Giraffe+ recommended).
2. Let Gradle sync. If sync fails, ensure Kotlin/Compose plugin versions match (see app/build.gradle.kts).
3. Run on a device or emulator (API 24+).

## Features
- Compose UI: Entry, History, Detail, Trends
- Room database for local storage
- Rule-based SentimentAnalyzer (replaceable with TFLite or cloud)
- WorkManager reminders + Notification channel
- MPAndroidChart trends via AndroidView
- Optional secure storage via EncryptedSharedPreferences

## Upgrade paths
- TFLite: add model.tflite to assets and replace RuleBasedSentimentAnalyzer with a TFLite wrapper using TensorFlow Lite Task Text.
- Cloud NLP: create a Retrofit service and call your endpoint from the repository before saving.

## Privacy tips
- Keep backups disabled for DB (configured). Consider field-level encryption if storing sensitive names.

## License
MIT
>>>>>>> fa79dfa (Added the app)
"# Dream-journal" 
