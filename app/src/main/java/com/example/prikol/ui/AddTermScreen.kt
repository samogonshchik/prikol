package com.example.prikol.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prikol.AppViewModelProvider
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

@Composable
fun EnterTermForm(
    termInfo: TermInfo,
    updateTermInfo: (TermInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(defaultOffset),
        modifier = modifier
    ) {
        Text(
            text = "Term name:"
        )
        TextField(
            value = termInfo.name,
            onValueChange = { updateTermInfo(termInfo.copy(name = it)) }
        )
        Text(
            text = "Term definition:"
        )
        TextField(
            value = termInfo.definition,
            onValueChange = { updateTermInfo(termInfo.copy(definition = it)) }
        )
    }
}