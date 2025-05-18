package com.example.prikol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.prikol.ui.theme.PrikolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//      to get window size correctly?
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this));
        }

        setContent {
            PrikolTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TestPython()
                }
            }
        }
    }
}

@Composable
fun TestPython() {
    Scaffold() { innerPadding ->
        val py = Python.getInstance()
        val main = py.getModule("main")
        val gridStr = main["grid"]?.asList()?.joinToString("\n")

        Text(text = "test_output:\n" + gridStr, modifier = Modifier.padding(innerPadding))
    }
}
