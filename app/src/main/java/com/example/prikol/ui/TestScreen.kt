import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Test() {
    Scaffold(

    ) { innerPadding ->
        val cells = remember {
            mutableStateListOf("A", "", "C", "") // 2x2 grid example
        }
        var selectedCell by remember { mutableStateOf<Int?>(null) }
        var tfv by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current


//            CELLS
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            repeat(2) { row ->
                Row {
                    repeat(2) { col ->
                        val cellIndex = row * 2 + col
                        CrosswordCell(
                            letter = cells[cellIndex],
                            isSelected = selectedCell == cellIndex,
                            onClick = {
                                focusRequester.requestFocus()
                                keyboardController?.show()
                                selectedCell = cellIndex
                                println("selected cell now: $selectedCell")
                                      },
//                            onClickWhenSelected = {
//                                // Action when clicking an already selected cell, e.g., show hint
//                                println("Clicked selected cell $cellIndex")
//                            },
//                            onLetterChanged = { newLetter ->
//                                cells[cellIndex] = newLetter
//                            },
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
            }
            TextField(
                value = tfv,
                onValueChange = {
                    tfv = if (tfv.isNotBlank() && it.isNotBlank()) {
                        it.last().toString()
                    } else {
                        it
                    }
                    if (selectedCell != null) cells[selectedCell!!] = tfv
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
//                    .size(1.dp)
                    .alpha(0f),
            )
        }
    }
}

@Composable
fun CrosswordCell(
    letter: String,
    isSelected: Boolean,
    onClick: () -> Unit,
//    onClickWhenSelected: () -> Unit,
//    onLetterChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .size(48.dp) // Square cell size
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                if (isSelected) {
//                    onClickWhenSelected()
                } else {
                    onClick()
                }
            },
//            .focusRequester(focusRequester)
//            .focusable()
//            .onKeyEvent { keyEvent ->
//                if (isSelected && keyEvent.type == KeyEventType.KeyDown) {
//                    val char = keyEvent.key.toString()
//                    // Accept only single letters (A-Z)
//                    if (char.length == 1 && char[0].isLetter()) {
//                        onLetterChanged(char.uppercase())
//                        true
//                    } else if (keyEvent.key == Key.Backspace) {
//                        onLetterChanged("") // Clear the cell
//                        true
//                    } else {
//                        false
//                    }
//                } else {
//                    false
//                }
//            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter.uppercase(),
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }

    // Request focus when the cell is selected
//    LaunchedEffect(isSelected) {
//        if (isSelected) {
//            focusRequester.requestFocus()
//        } else {
//            focusManager.clearFocus()
//        }
//    }
}


