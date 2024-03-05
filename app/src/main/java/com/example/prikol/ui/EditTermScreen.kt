package com.example.prikol.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prikol.AppViewModelProvider
import com.example.prikol.ui.theme.Purple80
import com.example.prikol.ui.viewModels.EditTermViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTermScreen(
    navigateBack: () -> Unit = {  },
    navigateHome: () -> Unit = {  },
    viewModel: EditTermViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    var deleteRequested by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.updateTerm()
                    }
                    navigateBack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Done"
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit term") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple80),
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
//                    var menuExpanded: Boolean by remember { mutableStateOf(false) }

                    IconButton(onClick = { deleteRequested = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "More"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        EnterTermForm(
            modifier = Modifier
                .padding(innerPadding)
                .padding(defaultOffset)
                .verticalScroll(rememberScrollState()),
            termInfo = viewModel.termUiState.termInfo,
            updateTermInfo = viewModel::updateUiState
        )

        if (deleteRequested) {
            AlertDialog(
                onDismissRequest = { deleteRequested = false },
                title = { Text(text = "Term deleting") },
                text = { Text("Are u sure?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deleteRequested = false
                            navigateHome()
                            coroutineScope.launch {
//                                delay(7_000L) // why tasks listed below won't be executed after delay?
                                viewModel.deleteTerm()
                            }
                        }
                    ) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteRequested = false }) {
                        Text(text = "No")
                    }
                }
            )
        }
    }
}