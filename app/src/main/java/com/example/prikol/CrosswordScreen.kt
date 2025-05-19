package com.example.prikol

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaquo.python.Python

@Composable
fun CrosswordScreen() {
    Scaffold { innerPadding ->
        val py = Python.getInstance()
        val main = py.getModule("main")
        val grid = main["grid"]!!.asList().map { it -> it.asList().map { it -> it.toString() } }

        CrosswordGrid(
            grid = grid,
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .border(BorderStroke(2.dp, Color.Black))
        )
    }
}

@Composable
fun CrosswordGrid(
    grid: List<List<String>>,
    modifier: Modifier = Modifier
) {
    // Convert the initial grid of strings to a grid of TextFieldValue
    var gridValues by remember {
        mutableStateOf(
            grid.map { row ->
                row.map { letter ->
                    TextFieldValue(text = letter, selection = TextRange(1))
                }
            }
        )
    }

    Column(
        modifier = modifier
    ) {
        gridValues.forEachIndexed { i, row ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEachIndexed { j, textFieldValue ->
                    CrosswordSquare(
                        initTextFieldValue = textFieldValue,
                        onUserInput = { newValue ->
                            // Update the grid with the new TextFieldValue
                            gridValues = gridValues.mapIndexed { r, rowValues ->
                                if (r == i) {
                                    rowValues.mapIndexed { c, value ->
                                        if (c == j) newValue.copy(text = newValue.text.last().toString(), selection = TextRange(1)) else value
                                    }
                                } else {
                                    rowValues
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                            .aspectRatio(1f)
                            .border(BorderStroke(1.dp, Color.Black))
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrosswordSquare(
    initTextFieldValue: TextFieldValue,
    onUserInput: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() } // Provide interaction source
    BasicTextField(
        value = initTextFieldValue,
        onValueChange = onUserInput,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = initTextFieldValue.text,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                    top = 0.dp, bottom = 0.dp, start = 0.dp, end = 0.dp
                ),
                interactionSource = interactionSource,
                visualTransformation = VisualTransformation.None
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        interactionSource = interactionSource, // Explicitly pass interaction source
        visualTransformation = VisualTransformation.None, // Explicitly pass visual transformation
        modifier = modifier
    )
}
