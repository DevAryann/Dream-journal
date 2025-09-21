package com.example.dreamjournal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dreamjournal.ServiceLocator
import com.example.dreamjournal.data.Dream
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(nav: NavController) {
    val context = LocalContext.current
    val repo = remember { ServiceLocator.repository(context) }
    var dreams by remember { mutableStateOf<List<Dream>>(emptyList()) }

    LaunchedEffect(Unit) {
        repo.dreams().collectLatest { dreams = it }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("History") }) }) { padding ->
        if (dreams.isEmpty()) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No dreams yet. Create one.")
            }
        } else {
            LazyColumn(Modifier.padding(padding)) {
                items(dreams) { d ->
                    ListItem(
                        headlineContent = { Text(d.title, fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("Mood ${d.mood}  ${"%+.2f".format(d.moodScore)}") },
                        modifier = Modifier.clickable { nav.navigate("detail/${d.id}") }
                    )
                    Divider()
                }
            }
        }
    }
}
