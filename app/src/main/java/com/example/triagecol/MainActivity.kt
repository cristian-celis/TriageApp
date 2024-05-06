package com.example.triagecol

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.example.triagecol.presentation.admin.AdminViewModel
import com.example.triagecol.presentation.navigation.AppNavigation
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.ui.theme.TraigeColTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TraigeColTheme {
                Box(modifier = Modifier.background(color = Color.White)) {
                    viewModel.initBack()
                    AppNavigation(viewModel)
                }
            }
        }
    }
}