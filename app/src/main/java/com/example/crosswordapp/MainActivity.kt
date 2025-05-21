package com.example.crosswordapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.crosswordapp.ui.theme.CrosswordAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        to get window size correctly?
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this));
        }

        setContent {
            CrosswordAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CrosswordAppNavHost(navController = rememberNavController())
                }
            }
        }
    }
}


