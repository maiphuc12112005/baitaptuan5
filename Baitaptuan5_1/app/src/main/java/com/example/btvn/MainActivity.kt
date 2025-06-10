package com.example.btvn

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.btvn.navigation.NavGraph
import com.example.btvn.ui.theme.BTVNTheme
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.AndroidEntryPoint

/**
 * Application class annotated with @HiltAndroidApp
 * This enables Hilt's code generation for dependency injection.
 */
@HiltAndroidApp
class BTVNApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Global initialization logic (e.g., FirebaseApp.initializeApp(this)) can go here.
    }
}

/**
 * Main Activity of the app.
 * Annotated with @AndroidEntryPoint to allow Hilt to inject dependencies.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BTVNTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
