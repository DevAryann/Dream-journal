package com.example.dreamjournal.ui.screens

import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.dreamjournal.ServiceLocator
import com.example.dreamjournal.data.Dream
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendsScreen(nav: NavController) {
    val context = LocalContext.current
    val repo = remember { ServiceLocator.repository(context) }
    var dreams by remember { mutableStateOf<List<Dream>>(emptyList()) }

    LaunchedEffect(Unit) {
        repo.dreams().collectLatest { dreams = it.sortedBy { it.timestamp } }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Trends") }) }
    ) { innerPadding -> // <-- Receive the PaddingValues here
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // <-- Apply the padding here
            factory = { ctx ->
                LineChart(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setNoDataText("No data yet")
                    description = Description().apply { text = "Mood over time" }
                }
            },
            update = { chart ->
                val entries = dreams.mapIndexed { idx, d -> Entry(idx.toFloat(), d.moodScore) }
                val dataSet = LineDataSet(entries, "Mood Score").apply {
                    // Use androidx.compose.ui.graphics.Color and convert to android.graphics.Color for the chart
                    this.color = android.graphics.Color.BLUE // MPAndroidChart uses android.graphics.Color
                    valueTextColor = android.graphics.Color.BLACK
                }
                chart.data = LineData(dataSet)
                chart.invalidate() // Redraw the chart
            }
        )
    }
}