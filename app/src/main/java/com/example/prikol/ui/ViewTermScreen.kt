package com.example.prikol.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prikol.AppViewModelProvider
import com.example.prikol.R
import com.example.prikol.ui.theme.Purple80
import com.example.prikol.ui.viewModels.ViewTermViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTermScreen(
    navigateBack: () -> Unit = {  },
    navigateToEdit: (Int) -> Unit = {  },
    navigateToView: (Int) -> Unit = {  },
    navigateToNearest: (Int) -> Unit = {  },
    viewModel: ViewTermViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val viewTermUiState by viewModel.viewTermUiState.collectAsState()
    val defaultOffset = dimensionResource(R.dimen.default_offset)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "View term") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple80),
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.NavigateBack_Button_descr)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navigateToEdit(viewTermUiState.term.id) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.GoToEdit_Button_descr)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val separator = when (viewTermUiState.term.type) {
            "Term" -> " — "
            "Theorem" -> ":\n"
            else -> "\n"
        }

        val definition: String = viewTermUiState.term.definition
        val definitionWords: List<String> = definition.split(Regex("""[ ,.:;]+"""))
        val delimiters: List<String> = definition.split(Regex("""\w+""")).drop(1)
        val names: List<String> by viewModel.allNames.collectAsState()
        val namesWords: List<List<String>> = names.map { name ->
            name.split(" ").map { word ->
                word.lowercase()
            }
        }

        val annotatedDefinition = buildAnnotatedString {   // Delegate to viewTermViewModel or new function
            append("Clickable definition:\n")
            var i = 0
            var j = 0
            val end = delimiters.size   // delimiters.size - 1 == definitionWords.size, but use of second causes error (before using .drop(1))
            while (i < end) {
                while (j < names.size) {
                    val nameLen = namesWords[j].size
                    if (i + nameLen <= end) {
                        if (definitionWords.slice(i until i + nameLen).map { it.lowercase() } == namesWords[j].map { it.lowercase() }) {
                            pushStringAnnotation(tag = "term", annotation = viewModel.getTermIdStream(names[j]).collectAsState().value.id.toString())
                            withStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                                for (k in 0 until nameLen - 1) {
                                    append(definitionWords[i + k])
                                    append(delimiters[i + k])
                                }
                                append(definitionWords[i + nameLen - 1])
                            }
                            pop()
                            append(delimiters[i + nameLen - 1])
                            i += nameLen
                            break
                        }
                    }
                    j++
                }
                if (j == names.size) {
                    append(definitionWords[i])
                    append(delimiters[i])
                    i++
                }
                j = 0
            }
            toAnnotatedString()
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(defaultOffset),
            modifier = Modifier
                .padding(innerPadding)
                .padding(defaultOffset)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Term id: ${viewTermUiState.term.id}")
            Text(text = "Term type: ${viewTermUiState.term.type}")
            Text(text = "${viewTermUiState.term.name}$separator${viewTermUiState.term.definition}")
            ClickableText(
                text = annotatedDefinition,
                style = LocalTextStyle.current,
                onClick = { offset ->
                    if (annotatedDefinition.getStringAnnotations(tag = "term", start = offset, end = offset).firstOrNull() != null) {
                        navigateToView(annotatedDefinition.getStringAnnotations(tag = "term", start = offset, end = offset).firstOrNull()?.item?.toInt()!!)
                    }
                }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(defaultOffset)
            ) {
                if (viewModel.prevId != null) {
                    Card(
                        shape = RectangleShape,
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, Color.Black),
                    ) {
                        Text(
                            text = "Go to prev term",
                            modifier = Modifier
                                .clickable { navigateToNearest(viewModel.prevId!!) }
                                .padding(defaultOffset / 2)
                        )
                    }
                }
                if (viewModel.nextId != null) {
                    Card(
                        shape = RectangleShape,
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, Color.Black),
                    ) {
                        Text(
                            text = "Go to next term",
                            modifier = Modifier
                                .clickable { navigateToNearest(viewModel.nextId!!) }
                                .padding(defaultOffset / 2)
                        )
                    }
                }
            }
        }
    }
}