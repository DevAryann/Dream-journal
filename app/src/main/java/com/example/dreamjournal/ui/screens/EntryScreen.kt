package com.example.dreamjournal.ui.screens

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dreamjournal.ServiceLocator
import com.example.dreamjournal.data.Dream
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EntryScreen(nav: NavController, recordPermission: PermissionState) {
    val context = LocalContext.current
    val repo = remember { ServiceLocator.repository(context) }
    val analyzer = remember { ServiceLocator.analyzer() }
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    var mood by remember { mutableStateOf("Neutral") }
    var score by remember { mutableStateOf(0f) }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        val text = matches?.firstOrNull()
        if (!text.isNullOrBlank()) {
            content = TextFieldValue(content.text + (if (content.text.isBlank()) "" else " ") + text)
        }
    }

    fun startSpeech() {
        if (!recordPermission.status.isGranted) {
            recordPermission.launchPermissionRequest()
            return
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your dream...")
        }
        speechLauncher.launch(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("New Dream") })
        },
        bottomBar = {
            BottomAppBar {
                Button(onClick = { nav.navigate("history") }, modifier = Modifier.padding(8.dp)) { Text("History") }
                Button(onClick = { nav.navigate("trends") }, modifier = Modifier.padding(8.dp)) { Text("Trends") }
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Dream") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 160.dp)
            )
            Spacer(Modifier.height(8.dp))
            Row {
                Button(onClick = ::startSpeech) { Text("Voice") }
                Spacer(Modifier.width(12.dp))
                Button(onClick = {
                    val res = analyzer.analyze(content.text)
                    mood = res.mood
                    score = res.score
                }) { Text("Analyze") }
                Spacer(Modifier.width(12.dp))
                Button(onClick = {
                    scope.launch {
                        val d = Dream(
                            title = title.ifBlank { "Untitled" },
                            content = content.text,
                            mood = mood,
                            moodScore = score,
                            tags = emptyList()
                        )
                        repo.save(d)
                        nav.navigate("history")
                    }
                }) { Text("Save") }
            }
            Spacer(Modifier.height(8.dp))
            Text("Mood: $mood (${"%+.2f".format(score)})")
            Spacer(Modifier.height(8.dp))
            Text("Tip: You can anonymize names before saving.", style = MaterialTheme.typography.bodySmall)
        }
    }
}
