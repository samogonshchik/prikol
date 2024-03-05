package com.example.prikol.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prikol.AppViewModelProvider
import com.example.prikol.ui.theme.Purple80
import com.example.prikol.ui.viewModels.ViewTermViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTermScreen(
    navigateBack: () -> Unit = {  },
    navigateToEdit: (Int) -> Unit = {  },
    navigateToNext: (Int) -> Unit = {  },
    viewModel: ViewTermViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val viewTermUiState by viewModel.viewTermUiState.collectAsState()
    val separator = when (viewTermUiState.term.type) {
        "Term" -> " — "
        "Theorem" -> ":\n"
        else -> "\n"
    }
//    val allNames by viewModel.allNames.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "View term") },
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
                    IconButton(onClick = { navigateToEdit(viewTermUiState.term.id) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(defaultOffset),
            modifier = Modifier
                .padding(innerPadding)
                .padding(defaultOffset)
        ) {
            Text(
                text = "Term id: ${viewTermUiState.term.id}"
            )
            Text(
                text = "Term type: ${viewTermUiState.term.type}"
            )
            Text(
                text = "${viewTermUiState.term.name}$separator${viewTermUiState.term.definition}"
            )

            if (viewModel.nextId != null) {
                Text(
                    text = "Go to next term",
                    modifier = Modifier.clickable { navigateToNext(viewModel.nextId!!) }
                )
            }
        }
    }
}