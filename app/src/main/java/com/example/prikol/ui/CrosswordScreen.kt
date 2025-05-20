package com.example.prikol.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaquo.python.PyException
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun CrosswordScreen(
    navigateHome: () -> Unit,
//    more complex data required for statistics
    navigateWin: (List<String>) -> Unit
) {
    Scaffold { innerPadding ->
        val py = Python.getInstance()
        val main = py.getModule("main")
        val grid = main["grid"]!!.asList().map { it -> it.asList().map { it.toString() } }
        val clues = parseClues(main)
        val wordCells = parseWordCells(main)
        val placedWords = mutableListOf<String>()

        val pyIterator = main.get("placed_words")!!.callAttr("__iter__")
        while (true) {
            try {
                val nextItem: PyObject? = pyIterator.callAttr("__next__")
                if (nextItem == null) break
                placedWords.add(nextItem.toString())
            } catch (e: PyException) {
                if (e.message?.contains("StopIteration") == true) break
                throw e
            }
        }

        Log.d("TAG", grid.joinToString("\n"))
        Log.d("TAG", placedWords.joinToString())
        Log.d("TAG", clues.toString())

        CrosswordGrid(
            filledGrid = grid,
            wordCells = wordCells,
            clues = clues,
            placedWords = placedWords,
            navigateHome = navigateHome,
            navigateWin = navigateWin,
            modifier = Modifier
                .padding(innerPadding)
                .padding(15.dp)
        )
    }
}

@Composable
fun CrosswordGrid(
    filledGrid: List<List<String>>,
    wordCells: Map<String, Map<Pair<Int, Int>, List<Pair<Int, Int>>>>,
    clues: Map<String, Map<Pair<Int, Int>, List<String>>>,
    placedWords: List<String>,
    navigateHome: () -> Unit,
    navigateWin: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAlert by remember { mutableStateOf(false) }
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text("Congratulations!") },
            text = { Text("You have successfully completed the crossword! Press \"Continue\" to proceed mark words you've guessed as learned") },
            confirmButton = {
                TextButton(onClick = {
                    showAlert = false
                    navigateWin(placedWords)
                }) {
                    Text("Continue")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAlert = false
                    navigateHome()
                }) {
                    Text("Home")
                }
            },
        )
    }
    var dir by remember { mutableStateOf("H") }

    var grid by remember {
        mutableStateOf(
            filledGrid.map { row ->
                row.map { letter ->
                    Cell(
                        TFV = TextFieldValue(
                            text = if (letter == "#") "#" else "",
                            selection = TextRange(0)
                        ),
                        isActive = if (letter == "#") false else true,
                        color = if (letter == "#") Color.Black else Color.White
                    )
                }
            }
        )
    }

    // Create FocusRequesters for each cell
    val focusRequesters = remember {
        List(filledGrid.size) { List(filledGrid[0].size) { FocusRequester() } }
    }
    Column (modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .border(BorderStroke(2.dp, Color.Black))
        ) {
    //        filling grid of CrosswordSquare's
            grid.forEachIndexed { i, row ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEachIndexed { j, cell ->

                        CrosswordSquare(
                            cellValue = cell,
                            onUserInput = { newTFV ->
                                val newText =
                                    if (newTFV.text.isEmpty()) "" else newTFV.text.last().toString()
                                if (newText != cell.TFV.text) {
    //                                Log.d("Crossword", "Updating cell ($i,$j) to: $newText")

                                    grid = grid.mapIndexed { r, rowValues ->
                                        if (r == i) {
                                            rowValues.mapIndexed { c, curCell ->
                                                if (c == j && newText != curCell.TFV.text) {
                                                    if (filledGrid[i][j] == newText) {
                                                        curCell.copy(
                                                            TFV = newTFV.copy(
                                                                text = newText,
                                                                selection = TextRange(newText.length)
                                                            ),
                                                            isActive = false,
                                                            color = Color.LightGray
                                                        )
                                                    } else {
                                                        curCell.copy(
                                                            TFV = newTFV.copy(
                                                                text = newText,
                                                                selection = TextRange(newText.length)
                                                            )
                                                        )
                                                    }
                                                } else curCell
                                            }
                                        } else rowValues
                                    }
                                    // Move focus to the next cell to the right if it exists and is active
                                    when (dir) {
                                        "H" -> {
                                            if (j + 1 < filledGrid[i].size) {
                                                focusRequesters[i][j + 1].requestFocus()
                                            }
                                        }

                                        "V" -> {
                                            if (i + 1 < filledGrid[i].size) {
                                                focusRequesters[i + 1][j].requestFocus()
                                            }
                                        }

                                        else -> {}
                                    }

                                }
                                val curGrid = grid.map { row ->
                                    row.map { cell ->
                                        cell.TFV.text
                                    }
                                }
                                if (curGrid == filledGrid) {
                                    showAlert = true
                                }
                            },
                            focusRequester = focusRequesters[i][j],
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .border(BorderStroke(1.dp, Color.Black))
                        )
                    }
                }
            }
        }
        Row() {
            Text(
                text = "Dir: ${dir} ${if (dir == "V") "↓" else "→"}",
                modifier = Modifier
                    .padding(0.dp, 15.dp)
                    .clickable {
                        when (dir) {
                            "H" -> dir = "V"
                            "V" -> dir = "H"
                            else -> dir = "H"
                        }
                    }
                    .border(BorderStroke(1.dp, Color.Black))
                    .padding(5.dp)
            )
            Text(
                text = "GO  WIN",
                modifier = Modifier
                    .padding(15.dp)
                    .clickable {
                        navigateWin(placedWords)
                    }
                    .border(BorderStroke(1.dp, Color.Black))
                    .padding(5.dp)
            )
        }


        Text(
            text = "CLUES:",
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Horizontally:\n" + clues["hor"]?.entries?.joinToString("\n") { "${it.key}: ${it.value}".trim { ", ".contains(it) } } +
                    "\nVertically:\n"  + clues["vert"]?.entries?.joinToString("\n") { "${it.key}: ${it.value}".trim { ", ".contains(it) } },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Black))
                .padding(15.dp)
                .verticalScroll(rememberScrollState())
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrosswordSquare(
    cellValue: Cell,
    onUserInput: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = cellValue.TFV,
        onValueChange = onUserInput,
        enabled = cellValue.isActive,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = cellValue.TFV.text,
                innerTextField = innerTextField,
                singleLine = true,
                enabled = cellValue.isActive,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = cellValue.color,
                    unfocusedContainerColor = cellValue.color,
                    focusedContainerColor = cellValue.color,
                    focusedIndicatorColor = cellValue.color,
                    unfocusedIndicatorColor = cellValue.color
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
            imeAction = ImeAction.Next
        ),
        interactionSource = interactionSource,
        visualTransformation = VisualTransformation.None,
        modifier = modifier.focusRequester(focusRequester)
    )
}

fun parseClues(module: PyObject?): Map<String, Map<Pair<Int, Int>, List<String>>> {
    val jsonString = module!!.callAttr("get_clues").toJava(String::class.java)
    val gson = Gson()
    val type = object : TypeToken<Map<String, Map<String, List<String>>>>() {}.type
    val rawData: Map<String, Map<String, List<String>>> = gson.fromJson(jsonString, type)
    return rawData.mapValues { (_, innerMap) ->
        innerMap.mapKeys { (key, _) ->
            val (x, y) = key.split(",").map { it.toInt() }
            Pair(x, y)
        }
    }
}

fun parseWordCells(module: PyObject?): Map<String, Map<Pair<Int, Int>, List<Pair<Int, Int>>>> {
    val jsonString = module!!.callAttr("get_word_cells").toJava(String::class.java)
    val gson = Gson()
    val type = object : TypeToken<Map<String, Map<String, List<String>>>>() {}.type
    val rawData: Map<String, Map<String, List<String>>> = gson.fromJson(jsonString, type)
    return rawData.mapValues { (_, innerMap) ->
        innerMap.mapKeys { (key, _) ->
            val (x, y) = key.split(",").map { it.toInt() }
            Pair(x, y)
        }.mapValues { (_, value) ->
            value.map { coord ->
                val (x, y) = coord.split(",").map { it.toInt() }
                Pair(x, y)
            }
        }
    }
}

data class Cell(
    val isActive: Boolean,
    val TFV: TextFieldValue,
    val color: Color
)