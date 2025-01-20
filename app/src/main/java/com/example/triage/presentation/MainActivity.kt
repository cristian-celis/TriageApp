package com.example.triage.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.triage.presentation.navigation.AppNavigation
import com.example.triage.ui.theme.TraigeColTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TraigeColTheme {
                Box(modifier = Modifier.background(color = Color.White)) {
                    viewModel.initAPI()
                    AppNavigation(viewModel)
                }
            }
        }
    }
}