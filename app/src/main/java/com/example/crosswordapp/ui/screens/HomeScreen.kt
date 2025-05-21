package com.example.crosswordapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

@Composable
fun HomeScreen(
    navigateNewGame: () -> Unit,
    navigateResumeGame: () -> Unit,
//    navigateRules: () -> Unit,
    navigateLearnedWords: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val context = LocalContext.current
        val gridFile = File(context.filesDir, "crossword_grid.json")


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "NEW GAME",
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 20.dp)
                    .clickable { navigateNewGame() }
                    .border(BorderStroke(1.dp, Color.Black))
                    .padding(10.dp)
            )
            if (gridFile.exists() && gridFile.readText().isNotBlank()) {
                Text(
                    text = "RESUME GAME",
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 20.dp)
                        .clickable { navigateResumeGame() }
                        .border(BorderStroke(1.dp, Color.Black))
                        .padding(10.dp)
                )
            }

//            Text(
//                text = "RULES",
//                fontSize = 30.sp,
//                modifier = Modifier
//                    .padding(0.dp, 0.dp, 0.dp, 20.dp)
//                    .clickable { navigateRules() }
//                    .border(BorderStroke(1.dp, Color.Black))
//                    .padding(10.dp)
//            )
            Text(
                text = "LEARNED WORDS",
                fontSize = 30.sp,
                modifier = Modifier
//                    .padding(0.dp, 0.dp, 0.dp, 15.dp)
                    .clickable { navigateLearnedWords() }
                    .border(BorderStroke(1.dp, Color.Black))
                    .padding(10.dp)
            )
        }
    }
}