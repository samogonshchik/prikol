package com.example.crosswordapp.ui.screens

import java.io.IOException
import android.content.Context
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

fun writeToFile(context: Context, fileName: String, content: String) {
    try {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output.write(content.toByteArray())
        }
    } catch (e: IOException) {
        e.printStackTrace()
        // Handle error (e.g., show a toast or log)
    }
}

fun readFromFile(context: Context, fileName: String): String {
    return try {
        context.openFileInput(fileName).bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
        // Handle error (e.g., return empty string or notify user)
        ""
    }
}

@Composable
fun LearnedWordsScreen(
    navigateHome: () -> Unit
) {
    Scaffold { innerPadding ->
        var learnedWords by remember { mutableStateOf("") }

        val context = LocalContext.current
        learnedWords = readFromFile(context, "learnedWords.txt")

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
            Column(

            ) {
                learnedWords.split(",").forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (it != "") {
                            Text(
                                text = "x",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(0.dp, 0.dp, 5.dp, 0.dp)
                                    .clickable {
                                        val newLearnedWords = learnedWords.replace("${it},", "")
                                        writeToFile(context, "learnedWords.txt", newLearnedWords)
                                        learnedWords = newLearnedWords
                                    }
                                    .padding(5.dp)
                            )
                            Text(it)
                        }
                    }
                }
            }
        }
    }
}