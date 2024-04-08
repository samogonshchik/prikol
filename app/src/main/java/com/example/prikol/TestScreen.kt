package com.example.prikol

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.prikol.ui.katex.Katex
import com.example.prikol.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(
    navigateHome: () -> Unit = {  },
){
    BackHandler(
        enabled = true,
        onBack = {
            navigateHome()
        }
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateHome() }
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Navigate home"
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Test screen") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple80),
                navigationIcon = {
                    IconButton(onClick = { navigateHome() }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Navigate home"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
//        val latexString = """M(1, \frac{y}{x^k}) h \dfrac{1}{x^m}M(x,y)"""
        val katexString = """d = \\ \int \frac{sin(x)}{2} \cdot dx"""

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text("Katex display mode:")
            Katex(katexString, displayMode = true)
        }
    }
}

