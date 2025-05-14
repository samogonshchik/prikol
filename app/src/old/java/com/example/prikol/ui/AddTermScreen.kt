package com.example.prikol.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prikol.AppViewModelProvider
import com.example.prikol.R
import com.example.prikol.ui.theme.Purple80
import com.example.prikol.ui.viewModels.AddTermViewModel
import com.example.prikol.ui.viewModels.TermInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTermScreen(
    navigateBack: () -> Unit = {  },
    viewModel: AddTermViewModel = viewModel(factory = AppViewModelProvider.Factory),
){
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveTerm()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.AddTerm_FAB_descr)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.AddTermScreen_title)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple80),
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.NavigateBack_Button_descr)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        EnterTermForm(
            termInfo = viewModel.termUiState.termInfo,
            updateTermInfo = viewModel::updateUiState,
            innerPadding = innerPadding,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.default_offset))
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EnterTermForm(
    termInfo: TermInfo,
    updateTermInfo: (TermInfo) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val defaultOffset = dimensionResource(R.dimen.default_offset)

    val definitionTfv = TextFieldValue(
        text = termInfo.definitionText,
        selection = termInfo.definitionCursor
    )
    val nameTfv = TextFieldValue(
        text = termInfo.nameText,
        selection = termInfo.nameCursor
    )
    var focusedElement by remember { mutableStateOf("") }

    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)
            .windowInsetsPadding(WindowInsets.ime)
    ) {
        val isImeVisible = WindowInsets.isImeVisible

        Column(
            verticalArrangement = Arrangement.spacedBy(defaultOffset),
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .weight(weight = 1f, fill = false)   // For "fill"
        ) {
            val focusManager = LocalFocusManager.current

            LaunchedEffect(
                key1 = isImeVisible
            ) {
                if (!isImeVisible) {
                    focusManager.clearFocus()
                    focusedElement = ""
                }
            }

//            if (!isImeVisible) focusManager.clearFocus()   // Probably focus removed before set

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                var menuExpanded: Boolean by remember { mutableStateOf(false) }

                Text(text = "Term type: ")
                Box {
                    Card(
                        shape = RectangleShape,
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, Color.Black),
                    ) {
                        Text(
                            text = termInfo.type,
                            modifier = Modifier
                                .clickable { menuExpanded = true }
                                .padding(defaultOffset / 2)
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Paragraph") },
                            onClick = {
                                updateTermInfo(termInfo.copy(type = "Paragraph"))
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Term") },
                            onClick = {
                                updateTermInfo(termInfo.copy(type = "Term"))
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Theorem") },
                            onClick = {
                                updateTermInfo(termInfo.copy(type = "Theorem"))
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Other") },
                            onClick = {
                                updateTermInfo(termInfo.copy(type = "Other"))
                                menuExpanded = false
                            }
                        )
                    }
                }
            }
            Text(text = "Term name:")
            TextField(
                value = nameTfv,
                onValueChange = { updateTermInfo(termInfo.copy(nameText = it.text, nameCursor = it.selection)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
//                    capitalization = KeyboardCapitalization.Sentences,   // Doesn't work with "Password" kType
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .onFocusChanged {
                        if (it.isFocused) focusedElement = "name"
                    }
            )
            Text(text = "Term definition:")
            TextField(
                value = definitionTfv,
                onValueChange = { updateTermInfo(termInfo.copy(definitionText = it.text, definitionCursor = it.selection)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
//                    capitalization = KeyboardCapitalization.Sentences,   // Doesn't work with "Password" kType
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .onFocusChanged {
                        if (it.isFocused) focusedElement = "definition"
                    }
            )
        }

        @Composable
        fun SpecialKey(
            label: String,
            textToInsert: String,
            shift: Int = 0
        ) {
            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(
                        start = defaultOffset / 2,
                        top = defaultOffset / 2,
                        bottom = defaultOffset / 2
                    )
                    .background(Color.Transparent)
                    .clickable {
                        if (focusedElement == "name") {
                            val newNameText =
                                termInfo.nameText.slice(0 until termInfo.nameCursor.start) + textToInsert + termInfo.nameText.slice(
                                    termInfo.nameCursor.end until termInfo.nameText.length
                                )
                            val leap =
                                if (shift != 0) shift else if (textToInsert.indexOf("{") != -1) textToInsert.indexOf(
                                    "{"
                                ) + 1 else textToInsert.length

                            updateTermInfo(
                                termInfo.copy(
                                    nameText = newNameText,
                                    nameCursor = TextRange(termInfo.nameCursor.start + leap)
                                )
                            )
                        } else if (focusedElement == "definition") {
                            val newDefinitionText =
                                termInfo.definitionText.slice(0 until termInfo.definitionCursor.start) + textToInsert + termInfo.definitionText.slice(
                                    termInfo.definitionCursor.end until termInfo.definitionText.length
                                )
                            val leap =
                                if (shift != 0) shift else if (textToInsert.indexOf("{") != -1) textToInsert.indexOf(
                                    "{"
                                ) + 1 else textToInsert.length

                            updateTermInfo(
                                termInfo.copy(
                                    definitionText = newDefinitionText,
                                    definitionCursor = TextRange(termInfo.definitionCursor.start + leap)
                                )
                            )
                        }

                    }
                    .padding(vertical = defaultOffset / 1.25f, horizontal = defaultOffset / 1.25f)
            )
        }

        val scrollState = rememberScrollState()   // Where to put?

        LaunchedEffect (scrollState) {
            scrollState.scrollTo(scrollState.maxValue)
        }

        if (isImeVisible) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray)
                    .horizontalScroll(scrollState)
            ) {
                SpecialKey(
                    label = "frac",
                    textToInsert = "\\frac{}{}",
                )
                SpecialKey(
                    label = "\\end{}",
                    textToInsert = "\\end{}",
                )
                SpecialKey(
                    label = "\\begin{}",
                    textToInsert = "\\begin{}",
                )
                SpecialKey(
                    label = "{}",
                    textToInsert = "{}",
                )
                SpecialKey(
                    label = "\$\$",
                    textToInsert = "\$\$\$\$",
                    shift = 2
                )
                SpecialKey(
                    label = "\$",
                    textToInsert = "\$\$",
                    shift = 1
                )
                SpecialKey(
                    label = "\\",
                    textToInsert = "\\",
                )
                SpecialKey(
                    label = "⇥",
                    textToInsert = "    ",
                )
                SpecialKey(
                    label = "↩",
                    textToInsert = "\n",
                )
            }
        }
    }
}