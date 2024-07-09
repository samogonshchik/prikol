package com.example.prikol.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.prikol.ui.katex.Katex
import com.example.prikol.ui.theme.Purple80
import com.example.prikol.ui.viewModels.ViewTermViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
        Column(
            verticalArrangement = Arrangement.spacedBy(defaultOffset),
            modifier = Modifier
                .padding(innerPadding)
                .padding(defaultOffset)
                .verticalScroll(rememberScrollState())
        ) {
//            Text(text = "Term id: ${viewTermUiState.term.id}")
//            Text(text = "Term type: ${viewTermUiState.term.type}")

            // Main content
            val names: List<String> by viewModel.allNames.collectAsState()

            val splitText = splitNMerge(
                string = viewTermUiState.term.definition,
                regex = Regex("\\\$\\\$([^\\\$]+)\\\$\\\$"),
                processOuter = { notDisplayModeKatex ->
                    splitNMerge(
                        string = notDisplayModeKatex,
                        regex = Regex("\\\$([^\\\$]+)\\\$"),
                        processOuter = { notKatex ->
                            splitNMerge(
                                string = notKatex,
                                regex = Regex("\\n+"),
                                processOuter = { listOf(it) }
                            )
                        }
                    )
                }
            )

            // Main content
            FlowRow (
                modifier = Modifier
            ) {
                if (viewTermUiState.term.type == "Term") Text("    ")
                splitTextIntoParts(
                    text = viewTermUiState.term.name,
                    names = listOf(),
                    navigateToView = {  },
                    viewModel = viewModel
                )
                when (viewTermUiState.term.type) {
                    "Term" -> Text(" — ")
                    "Theorem" -> {
                        Text(":")
                        Spacer(modifier = Modifier.fillMaxWidth())
                    }
                    else -> Spacer(modifier = Modifier.fillMaxWidth())
                }
                splitText.forEach {
                    if (Regex("\\\$\\\$([^\\\$]+)\\\$\\\$") matches it) {
                        Spacer(modifier = Modifier.fillMaxWidth())
                        Katex(it.trim('\$'), displayMode = true)
                        Spacer(modifier = Modifier.fillMaxWidth())
                    } else if(Regex("\\\$([^\\\$]+)\\\$") matches it) {
                        Katex(it.trim('\$'))
                    } else if(Regex("\\n+") matches it) {
                        newlinesToComposables(it).forEach { newline ->
                            newline
                        }
                    } else {
                        splitTextIntoParts(text = it,
                            names = names,
                            navigateToView = navigateToView,
                            viewModel = viewModel
                        ).forEach { word ->
                            word
                        }
                    }
                }
            }

            // "Prev" and "Next" buttons
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

@Composable
fun splitTextIntoParts(
    text: String,
    names: List<String>,
    navigateToView: (Int) -> Unit,
    viewModel: ViewTermViewModel,
    textModifier: Modifier = Modifier,
    toSearch: Boolean = true
): List<Unit> {
//    val coroutineScope = rememberCoroutineScope()
    val textParts = mutableListOf<Unit>()

    if (!toSearch) {
        Regex("""\w+""").findAll(text).forEach {
            textParts.add(Text(it.value))
        }
        return textParts
    }

    val definitionWords: List<String> = Regex("""\w+""").findAll(text).map { it.value }.toList()
    val namesWords: List<List<String>> = names.map { name ->
        name.split(" ").map { word ->
            word.lowercase()
        }
    }
    val delimiters: List<String> = text.split(Regex("""\w+""")).let {
        if (it[0] != "") textParts.add(Text(it[0]))
        it.drop(1)
    }

    var i = 0
    var j = 0
    val end = delimiters.size   // delimiters.size - 1 == definitionWords.size, but use of second causes error (before using .drop(1))
    while (i < end) {
        while (j < names.size) {
            val nameLen = namesWords[j].size
            if (i + nameLen <= end) {
                if (definitionWords.slice(i until i + nameLen).map { it.lowercase() } == namesWords[j].map { it.lowercase() }) {
                    val clickableText = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                            for (k in 0 until nameLen - 1) {
                                append(definitionWords[i + k])
                                append(delimiters[i + k])
                            }
                            append(definitionWords[i + (nameLen - 1)])
                        }
                        append(delimiters[i + (nameLen - 1)])
                        toAnnotatedString()
                    }
                    val id = viewModel.getTermIdStream(names[j]).collectAsState().value.id

                    textParts.add(
                        ClickableText(
                            text = clickableText,
                            style = LocalTextStyle.current,
                            onClick = {
                                navigateToView(id)
                            },
                            modifier = textModifier
                        )
                    )

                    i += nameLen
                    break
                }
            }
            j++
        }
        if (j == names.size) {
            textParts.add(
                Text(
                    text = definitionWords[i] + delimiters[i],
//                    modifier = Modifier.border(BorderStroke(1.dp, Color.Blue))
                )
            )
            i++
        }
        j = 0
    }

    return textParts
}

fun mergeLists(outerList: List<List<String>>, innerList: List<String>): List<String> {
    val resultList: MutableList<String> = mutableListOf()
    if (outerList.size - 1 == innerList.size) {
        for (i in outerList.dropLast(1).indices) {
            outerList[i].forEach {
                resultList.add(it)
            }
            resultList.add(innerList[i])
        }
        outerList.last().forEach {
            resultList.add(it)
        }
    }
//    Log.e("TEST", "merged from ${outerList.size} and ${innerList.size} to ${resultList.size}")
    return resultList
}

@Composable
fun newlinesToComposables(s: String): List<Unit> {
    val resultList: MutableList<Unit> = mutableListOf(Spacer(modifier = Modifier.fillMaxWidth()))
    if (Regex("""\n+""").matches(s)) {
        for (i in s.dropLast(1).indices) {
            resultList.add(
                Text("", modifier = Modifier
                    .fillMaxWidth()
                )
            )
        }
    }
    return resultList
}

fun splitNMerge(
    string: String,
    regex: Regex,
    processOuter: (String) -> List<String>,
): List<String> {
    val outerList = string.split(regex)
    val innerList = regex.findAll(string).map { it.value }
    val newInnerList = mutableListOf<String>()
    val newOuterList = mutableListOf<List<String>>()

    outerList.forEach {
        newOuterList.add(processOuter(it))
    }
    innerList.forEach {
        newInnerList.add(it)
    }

    return mergeLists(newOuterList, newInnerList)
}