package com.example.dreamjournal.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dreamjournal.ui.screens.DetailScreen
import com.example.dreamjournal.ui.screens.EntryScreen
import com.example.dreamjournal.ui.screens.HistoryScreen
import com.example.dreamjournal.ui.screens.TrendsScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { App() }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun App() {
    val nav = rememberNavController()
    val recordPermission = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        NavHost(navController = nav, startDestination = "entry") {
            composable("entry") { EntryScreen(nav, recordPermission) }
            composable("history") { HistoryScreen(nav) }
            composable("detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L
                DetailScreen(nav, id)
            }
            composable("trends") { TrendsScreen(nav) }
        }
    }
}
