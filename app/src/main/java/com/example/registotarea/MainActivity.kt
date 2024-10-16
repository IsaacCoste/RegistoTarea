package com.example.registotarea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.registotarea.presentation.navigation.TareaNavHost
import com.example.registotarea.ui.theme.RegistoTareaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistoTareaTheme {
                val navHost = rememberNavController()
                TareaNavHost(navHost)
            }
        }
    }
}