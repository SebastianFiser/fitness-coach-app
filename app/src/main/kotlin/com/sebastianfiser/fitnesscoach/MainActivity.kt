package com.sebastianfiser.fitnesscoach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.activity.compose.
import androidx.activity.compose.WindowCompat
import androidx.xompose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
           StartContent()
         }
    }

    @Composable
    fun StartContent() {
        Box (
            modifier = Modifier
            .fillMaxSize
            .background(Color.Gray)
            
        ) {
            
        }
    }
}
