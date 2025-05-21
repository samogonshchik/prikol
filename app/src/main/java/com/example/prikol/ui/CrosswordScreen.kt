import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.prikol.ui.clues
import com.example.prikol.ui.filledGrid
import com.example.prikol.ui.wordCells
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CrosswordScreen(
    newGameQ: Boolean = true
) {
    Scaffold(

    ) { innerPadding ->
        var selectedCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
        var tfv by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current

        val context = LocalContext.current
        val gridFile = File(context.filesDir, "crossword_grid.json")
        val grid = remember {
            val initialGrid = mutableStateListOf(
                mutableListOf(*filledGrid[0].map { if (it == "#") "#" else "" }.toTypedArray()),
                mutableListOf(*filledGrid[1].map { if (it == "#") "#" else "" }.toTypedArray()),
                mutableListOf(*filledGrid[2].map { if (it == "#") "#" else "" }.toTypedArray()),
                mutableListOf(*filledGrid[3].map { if (it == "#") "#" else "" }.toTypedArray()),
                mutableListOf(*filledGrid[4].map { if (it == "#") "#" else "" }.toTypedArray()),
                mutableListOf(*filledGrid[5].map { if (it == "#") "#" else "" }.toTypedArray()),
                mutableListOf(*filledGrid[6].map { if (it == "#") "#" else "" }.toTypedArray()),
                mutableListOf(*filledGrid[7].map { if (it == "#") "#" else "" }.toTypedArray()),
                mutableListOf(*filledGrid[8].map { if (it == "#") "#" else "" }.toTypedArray()),
                mutableListOf(*filledGrid[9].map { if (it == "#") "#" else "" }.toTypedArray())
            )
            if (!newGameQ && gridFile.exists()) {
                try {
                    val savedGrid = Json.decodeFromString<List<List<String>>>(gridFile.readText())
                    mutableStateListOf(*savedGrid.map { mutableListOf(*it.toTypedArray()) }.toTypedArray())
                } catch (e: Exception) {
                    println("Failed to load grid: $e")
                    initialGrid
                }
            } else {
                initialGrid
            }
        }
        val availableCells = remember {
            mutableStateListOf(
                mutableListOf(*filledGrid[0].map { it != "#" }.toTypedArray()),
                mutableListOf(*filledGrid[1].map { it != "#" }.toTypedArray()),
                mutableListOf(*filledGrid[2].map { it != "#" }.toTypedArray()),
                mutableListOf(*filledGrid[3].map { it != "#" }.toTypedArray()),
                mutableListOf(*filledGrid[4].map { it != "#" }.toTypedArray()),
                mutableListOf(*filledGrid[5].map { it != "#" }.toTypedArray()),
                mutableListOf(*filledGrid[6].map { it != "#" }.toTypedArray()),
                mutableListOf(*filledGrid[7].map { it != "#" }.toTypedArray()),
                mutableListOf(*filledGrid[8].map { it != "#" }.toTypedArray()),
                mutableListOf(*filledGrid[9].map { it != "#" }.toTypedArray())
            )
        }
        fun checkAndDisableWord(wordCells: List<Pair<Int, Int>>) {
            println("checking word")
            val word = wordCells.map { grid[it.first][it.second] }.joinToString("")
            val correctWord = wordCells.map { filledGrid[it.first][it.second] }.joinToString("")
            if (word == correctWord && word.isNotEmpty() && wordCells.all { grid[it.first][it.second] != "#" }) {
                println("disabling word")
                wordCells.forEach { (row, col) ->
                    availableCells[row][col] = false
                }
                selectedCell = null
//                mb remove hiding keyboard
                keyboardController?.hide()
            }
        }
        fun checkAllWords() {
            wordCells["hor"]?.values?.forEach { wordCells ->
                checkAndDisableWord(wordCells)
            }
            wordCells["vert"]?.values?.forEach { wordCells ->
                checkAndDisableWord(wordCells)
            }
        }
//        checkAllWords()

        var dir by remember { mutableStateOf("h") }
        fun getSelectedWord(
            selectedCell: Pair<Int, Int>?,
            dir: String,
            wordCells: Map<String, Map<Pair<Int, Int>, List<Pair<Int, Int>>>>
        ): List<Pair<Int, Int>>? {
            if (selectedCell == null) return null
            val direction = if (dir == "h") "hor" else "vert"
            return wordCells[direction]?.entries?.find { entry ->
                entry.value.contains(selectedCell)
            }?.value
        }
        fun getAvailableDirections(
            cell: Pair<Int, Int>,
            wordCells: Map<String, Map<Pair<Int, Int>, List<Pair<Int, Int>>>>
        ): List<String> {
            val directions = mutableListOf<String>()
            if (wordCells["hor"]?.entries?.any { it.value.contains(cell) } == true) {
                directions.add("h")
            }
            if (wordCells["vert"]?.entries?.any { it.value.contains(cell) } == true) {
                directions.add("v")
            }
            return directions
        }

        // Get the currently selected word
        val selectedWord = getSelectedWord(selectedCell, dir, wordCells)

        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(10.dp)
//                .border(BorderStroke(1.dp, Color.Black))
        ) {


            grid.forEachIndexed { r, row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    row.forEachIndexed { c, elem ->
                        CrosswordCell(
                            letter = elem,
                            isSelected = selectedCell == Pair(r, c),
                            isEnabled = availableCells[r][c],
                            onClick = {
                                val availableDirections = getAvailableDirections(Pair(r, c), wordCells)
                                dir = availableDirections[0]
                                focusRequester.requestFocus()
                                keyboardController?.show()
                                selectedCell = Pair(r, c)
                                println("selected cell now: $selectedCell")
                            },
                            onClickWhenSelected = {
                                if (availableCells[r][c]) {
                                    val availableDirections = getAvailableDirections(Pair(r, c), wordCells)
                                    if (availableDirections.size > 1) {
                                        // Toggle to the other direction if both are available
                                        dir = if (dir == "h") "v" else "h"
                                    } else if (availableDirections.isNotEmpty() && dir != availableDirections[0]) {
                                        // Switch to the only available direction if different
                                        dir = availableDirections[0]
                                    }
                                    println("clicked selected cell ${Pair(r, c)}, direction now $dir")
                                }
                            },
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(1f)
//                                .padding(1.dp)
                        )
                    }
                }
            }

//            hidden TextField for input (required to show IME)
            TextField(
                value = tfv,
                onValueChange = { newValue ->
                    val oldTfv = tfv
                    tfv = if (oldTfv.isNotBlank() && newValue.isNotBlank()) {
                        newValue.last().toString()
                    } else {
                        newValue
                    }
                    if (selectedCell != null && availableCells[selectedCell!!.first][selectedCell!!.second]) {
                        val currentCell = selectedCell!!
                        if (tfv != oldTfv) { // Only update if input changed
                            grid[currentCell.first][currentCell.second] = tfv
                            // Check both horizontal and vertical words
                            val horizontalWord = getSelectedWord(currentCell, "h", wordCells)
                            val verticalWord = getSelectedWord(currentCell, "v", wordCells)
                            if (horizontalWord != null) {
                                checkAndDisableWord(horizontalWord)
                            }
                            if (verticalWord != null) {
                                checkAndDisableWord(verticalWord)
                            }
                            // Move to next available cell in current word
                            val currentWord = getSelectedWord(currentCell, dir, wordCells)
                            if (currentWord != null && tfv.isNotBlank()) {
                                val currentIndex = currentWord.indexOf(currentCell)
                                val nextIndex = (currentIndex + 1)
                                if (nextIndex < currentWord.size) {
                                    val nextCell = currentWord[nextIndex]
                                    if (availableCells[nextCell.first][nextCell.second]) {
                                        selectedCell = nextCell
                                        focusRequester.requestFocus()
                                        keyboardController?.show()
                                    }
                                }
                            }
                            // Save grid to file
                            try {
                                val gridToSave = grid.map { it.toList() }.toList()
                                gridFile.writeText(Json.encodeToString(gridToSave))
                                println("Grid saved to file")
                                println("SAVED: " + Json.encodeToString(gridToSave))
                            } catch (e: Exception) {
                                println("Failed to save grid: $e")
                            }
                        }
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .visibility(false)
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            selectedCell = null
                        }
                    }
                    .size(1.dp)
            )

            Text(
                text = "Direction: ${if (dir == "h") "Horizontal" else "Vertical"}",
                style = MaterialTheme.typography.bodyLarge
            )

            if (selectedWord != null) {
                val clueDirection = if (dir == "h") "hor" else "vert"
                val clue = clues[clueDirection]?.get(selectedWord.first())?.firstOrNull() ?: "No clue available"
                Text(
                    text = "Clue: $clue",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

//            mb to change and "opt-out" of ExperimentalLayoutApi
            val isImeVisible = WindowInsets.isImeVisible
            LaunchedEffect(isImeVisible) {
                if (!isImeVisible && selectedCell != null) {
                    selectedCell = null
                }
            }
        }
    }
}

@Composable
fun CrosswordCell(
    letter: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
    onClickWhenSelected: () -> Unit = {  },
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
//            .size(48.dp)
            .background(
                color = when {
                    letter == "#" -> Color.Black
                    isSelected -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(4.dp) // Add this line
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(
                enabled = isEnabled // Use isEnabled to control clickability
            ) {
                if (isSelected) {
                    onClickWhenSelected()
                } else {
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter.uppercase(),
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

fun Modifier.visibility(visible: Boolean): Modifier {
    return layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        layout(placeable.width, placeable.height) {
            if (visible) {
                // place this item in the original position
                placeable.placeRelative(0, 0)
            }
        }
    }
}

