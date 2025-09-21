# Keep models used by Room
-keep class com.example.dreamjournal.data.** { *; }
# Keep Kotlin metadata
-keepclassmembers class kotlin.Metadata { *; }
# MPAndroidChart uses reflection for dataset labels sometimes
-keep class com.github.mikephil.charting.** { *; }
