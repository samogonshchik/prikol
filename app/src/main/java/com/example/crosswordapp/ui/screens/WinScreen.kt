package com.example.crosswordapp.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WinScreen(
    placedWords: List<String>,
    navigateHome: () -> Unit
) {
    Scaffold { innerPadding ->
        val context = LocalContext.current
        var guessedWords by remember { mutableStateOf(placedWords) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(15.dp, 0.dp, 15.dp, 15.dp)
        ) {
            Text(
                text = "LEARNED WORDS",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        navigateHome()
                    }
                    .padding(15.dp)
            )
            Column {
                guessedWords.forEach { word ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✓",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 5.dp, 0.dp)
                                .clickable {
                                    val learnedWords = readFromFile(context, "learnedWords.txt")
                                    writeToFile(context, "learnedWords.txt", learnedWords + word + ",")
                                    guessedWords = guessedWords.toMutableList().apply { remove(word) }
                                    Log.d("TAG", guessedWords.toString())
                                }
                                .padding(5.dp)
                        )
                        Text(word)
                    }
                }
            }
        }
    }
}