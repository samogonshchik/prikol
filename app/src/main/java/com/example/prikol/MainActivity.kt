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
import androidx.navigation.compose.rememberNavController
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.prikol.navigation.PrikolNavHost
import com.example.prikol.ui.theme.PrikolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this));
        }

        setContent {
            PrikolTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    PrikolNavHost(rememberNavController())
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
        val m = py.getModule("CrosswordGen2")
        val my_a = m.get("a").toString()

        Text(text = "hello, bitch, I'm there\n" + my_a, modifier = Modifier.padding(innerPadding))
    }
}
