package com.example.workmanagerandroid.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BlurScreen(
    showProgress: Boolean,
    blurClick: () -> Unit,
    fileClick: () -> Unit
) {
    var showFileBtn by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(showProgress) {
            CircularProgressIndicator()
            showFileBtn = true
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = blurClick) {
            Text("Start Blur")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if(!showProgress && showFileBtn) {
            Button(onClick = fileClick) {
                Text("Show Image")
            }
        }


    }
}