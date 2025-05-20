package com.example.prikol.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaquo.python.Python

@Composable
fun HomeScreen(
    navigateGame: () -> Unit,
    navigateRules: () -> Unit,
    navigateLearnedWords: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "GAME",
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 20.dp)
                    .clickable { navigateGame() }
                    .border(BorderStroke(1.dp, Color.Black))
                    .padding(10.dp)
            )
            Text(
                text = "RULES",
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 20.dp)
                    .clickable { navigateRules() }
                    .border(BorderStroke(1.dp, Color.Black))
                    .padding(10.dp)
            )
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