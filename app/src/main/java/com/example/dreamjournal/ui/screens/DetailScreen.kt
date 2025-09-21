package com.example.dreamjournal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dreamjournal.ServiceLocator
import com.example.dreamjournal.data.Dream
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(nav: NavController, id: Long) {
    val context = LocalContext.current
    val repo = remember { ServiceLocator.repository(context) }
    var dream by remember { mutableStateOf<Dream?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) {
        dream = repo.dream(id).filterNotNull().first()
    }

    Scaffold(topBar = { TopAppBar(title = { Text(dream?.title ?: "Detail") }) }) { padding ->
        dream?.let { d ->
            Column(Modifier.padding(padding).padding(16.dp)) {
                Text("Mood: ${d.mood}  (${"%+.2f".format(d.moodScore)})")
                Spacer(Modifier.height(8.dp))
                Text(d.content)
                Spacer(Modifier.height(12.dp))
                Button(onClick = {
                    scope.launch {
                        repo.delete(d)
                        nav.popBackStack()
                    }
                }) { Text("Delete") }
            }
        } ?: run {
            Box(Modifier.padding(padding).fillMaxSize()) { Text("Loading...") }
        }
    }
}
