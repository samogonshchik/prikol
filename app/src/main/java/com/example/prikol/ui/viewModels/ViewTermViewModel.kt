package com.example.prikol.ui.viewModels

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prikol.data.Term
import com.example.prikol.data.TermDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewTermViewModel(
    savedStateHandle: SavedStateHandle,
    private val termDao: TermDao   // Why val declaration wasn't needed here?
) : ViewModel() {
//    val termId: Int = checkNotNull(savedStateHandle["termId"])   // Why it doesn't throw any mistakes?
    private val stringTermId: String = checkNotNull(savedStateHandle["termId"])
    private val termId = stringTermId.toInt()
    var nextId: Int? = null
    var prevId: Int? = null

    val viewTermUiState: StateFlow<ViewTermUiState> = termDao.getTermStream(termId).map { ViewTermUiState(term = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(0),   //5_000L),
            initialValue = ViewTermUiState()
        )

    val allNames: StateFlow<List<String>> = termDao.getAllNamesStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(0),
        initialValue = listOf()
    )

    fun getTermIdStream(name: String): StateFlow<Term> = termDao.getTermStream(name).stateIn(
        scope = viewModelScope,
        SharingStarted.WhileSubscribed(0),
        initialValue = Term()
    )

    init {
        viewModelScope.launch {
            nextId = termDao.getNextId(termId)
            prevId = termDao.getPrevId(termId)
        }
    }
}

data class ViewTermUiState(
    val namesList: List<String> = listOf(),
    val term: Term = Term()
)