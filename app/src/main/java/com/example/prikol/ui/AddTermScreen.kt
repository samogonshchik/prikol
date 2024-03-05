package com.example.prikol.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prikol.AppViewModelProvider
import com.example.prikol.ui.theme.Purple80
import com.example.prikol.ui.viewModels.AddTermViewModel
import com.example.prikol.ui.viewModels.TermInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
                    contentDescription = "Add term"
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Add term") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple80),
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        EnterTermForm(
            termInfo = viewModel.termUiState.termInfo,
            updateTermInfo = viewModel::updateUiState,
            modifier = Modifier
                .padding(innerPadding)
                .padding(defaultOffset)
                .verticalScroll(rememberScrollState())
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EnterTermForm(
    termInfo: TermInfo,
    updateTermInfo: (TermInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded: Boolean by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    if (!WindowInsets.isImeVisible) focusManager.clearFocus()

    Column(
        verticalArrangement = Arrangement.spacedBy(defaultOffset),
        modifier = modifier
    ) {
        Row() {
            Text(text = "Term type: ")
            Box() {
                Text(
                    text = termInfo.type,
                    modifier = Modifier.clickable { menuExpanded = true }
                )
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
        Text(
            text = "Term name:"
        )
        TextField(
            value = termInfo.name,
            onValueChange = { updateTermInfo(termInfo.copy(name = it)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )
        Text(
            text = "Term definition:"
        )
        TextField(
            value = termInfo.definition,
            onValueChange = { updateTermInfo(termInfo.copy(definition = it)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            )
        )
    }
}