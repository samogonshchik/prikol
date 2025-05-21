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
//        val py = Python.getInstance()
//        val main = py.getModule("main")
//        val grid = main["grid"]!!.asList().map { it -> it.asList().map { it.toString() } }
//        val clues = parseClues(main)
//        val wordCells = parseWordCells(main)
//        val placedWords = mutableListOf<String>()
//
//        val pyIterator = main.get("placed_words")!!.callAttr("__iter__")
//        while (true) {
//            try {
//                val nextItem: PyObject? = pyIterator.callAttr("__next__")
//                if (nextItem == null) break
//                placedWords.add(nextItem.toString())
//            } catch (e: PyException) {
//                if (e.message?.contains("StopIteration") == true) break
//                throw e
//            }
//        }

        // Grid: 2D List of Characters (including '#')
        val grid = listOf(
            listOf("s", "t", "a", "t", "e", "m", "e", "n", "t", "#"),
            listOf("#", "r", "#", "o", "#", "e", "#", "o", "#", "a"),
            listOf("d", "e", "t", "o", "n", "a", "t", "o", "r", "s"),
            listOf("#", "y", "#", "k", "#", "d", "#", "d", "#", "s"),
            listOf("a", "#", "a", "#", "g", "o", "a", "l", "i", "e"),
            listOf("n", "e", "p", "h", "e", "w", "#", "e", "#", "t"),
            listOf("t", "#", "o", "#", "n", "#", "m", "#", "n", "#"),
            listOf("s", "p", "l", "a", "t", "t", "e", "r", "e", "d"),
            listOf("y", "#", "l", "#", "l", "#", "n", "#", "a", "#"),
            listOf("#", "b", "o", "d", "y", "g", "u", "a", "r", "d")
        )

// Clues: Map with Pair<Int, Int> as keys and List of Strings as values, nested under "hor" and "vert"
        val clues = mapOf(
            "hor" to mapOf(
                Pair(0, 0) to listOf("A declaration or remark"),
                Pair(2, 0) to listOf("A device used to detonate an explosive device etc"),
                Pair(4, 4) to listOf("A goalkeeper or goaltender"),
                Pair(5, 0) to listOf("A son of one's sibling, brother-in-law, or sister-in-law"),
                Pair(7, 0) to listOf("To splash"),
                Pair(9, 1) to listOf("To act as bodyguard for")
            ),
            "vert" to mapOf(
                Pair(0, 1) to listOf("A playing card or die with the rank of three"),
                Pair(0, 3) to listOf("To get into one's hands, possession or control, with or without force"),
                Pair(0, 5) to listOf("A field or pasture"),
                Pair(0, 7) to listOf("To think or ponder"),
                Pair(1, 9) to listOf("Something or someone of any value"),
                Pair(4, 0) to listOf("Restless, apprehensive and fidgety"),
                Pair(4, 2) to listOf("A very handsome young man"),
                Pair(4, 4) to listOf("In a gentle manner"),
                Pair(6, 6) to listOf("a bill of fare"),
                Pair(6, 8) to listOf("At or towards a position close in space or time")
            )
        )

// Word Cells: Map with Pair<Int, Int> as keys and List of Pair<Int, Int> as values, nested under "hor" and "vert"
        val wordCells = mapOf(
            "hor" to mapOf(
                Pair(0, 0) to listOf(
                    Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3), Pair(0, 4),
                    Pair(0, 5), Pair(0, 6), Pair(0, 7), Pair(0, 8)
                ),
                Pair(2, 0) to listOf(
                    Pair(2, 0), Pair(2, 1), Pair(2, 2), Pair(2, 3), Pair(2, 4),
                    Pair(2, 5), Pair(2, 6), Pair(2, 7), Pair(2, 8), Pair(2, 9)
                ),
                Pair(4, 4) to listOf(
                    Pair(4, 4), Pair(4, 5), Pair(4, 6), Pair(4, 7), Pair(4, 8), Pair(4, 9)
                ),
                Pair(5, 0) to listOf(
                    Pair(5, 0), Pair(5, 1), Pair(5, 2), Pair(5, 3), Pair(5, 4), Pair(5, 5)
                ),
                Pair(7, 0) to listOf(
                    Pair(7, 0), Pair(7, 1), Pair(7, 2), Pair(7, 3), Pair(7, 4),
                    Pair(7, 5), Pair(7, 6), Pair(7, 7), Pair(7, 8), Pair(7, 9)
                ),
                Pair(9, 1) to listOf(
                    Pair(9, 1), Pair(9, 2), Pair(9, 3), Pair(9, 4), Pair(9, 5),
                    Pair(9, 6), Pair(9, 7), Pair(9, 8), Pair(9, 9)
                )
            ),
            "vert" to mapOf(
                Pair(0, 1) to listOf(Pair(0, 1), Pair(1, 1), Pair(2, 1), Pair(3, 1)),
                Pair(0, 3) to listOf(Pair(0, 3), Pair(1, 3), Pair(2, 3), Pair(3, 3)),
                Pair(0, 5) to listOf(
                    Pair(0, 5), Pair(1, 5), Pair(2, 5), Pair(3, 5), Pair(4, 5), Pair(5, 5)
                ),
                Pair(0, 7) to listOf(
                    Pair(0, 7), Pair(1, 7), Pair(2, 7), Pair(3, 7), Pair(4, 7), Pair(5, 7)
                ),
                Pair(1, 9) to listOf(
                    Pair(1, 9), Pair(2, 9), Pair(3, 9), Pair(4, 9), Pair(5, 9)
                ),
                Pair(4, 0) to listOf(
                    Pair(4, 0), Pair(5, 0), Pair(6, 0), Pair(7, 0), Pair(8, 0)
                ),
                Pair(4, 2) to listOf(
                    Pair(4, 2), Pair(5, 2), Pair(6, 2), Pair(7, 2), Pair(8, 2), Pair(9, 2)
                ),
                Pair(4, 4) to listOf(
                    Pair(4, 4), Pair(5, 4), Pair(6, 4), Pair(7, 4), Pair(8, 4), Pair(9, 4)
                ),
                Pair(6, 6) to listOf(Pair(6, 6), Pair(7, 6), Pair(8, 6), Pair(9, 6)),
                Pair(6, 8) to listOf(Pair(6, 8), Pair(7, 8), Pair(8, 8), Pair(9, 8))
            )
        )

// Placed Words: List of Strings
        val placedWords = listOf(
            "detonators", "asset", "took", "trey", "statement", "noodle",
            "meadow", "goalie", "nephew", "gently", "bodyguard", "apollo",
            "splattered", "menu", "antsy", "near"
        )

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