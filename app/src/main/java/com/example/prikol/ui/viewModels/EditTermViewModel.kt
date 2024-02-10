package com.example.prikol.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prikol.data.Term
import com.example.prikol.data.TermDao
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditTermViewModel(
    savedStateHandle: SavedStateHandle,
    private val termDao: TermDao // why use private val, why not all the places of code can use it?
) : ViewModel() {
    private val stringTermId: String = checkNotNull(savedStateHandle["termId"])
    private val termId = stringTermId.toInt()

    var termUiState by mutableStateOf(TermUiState())
        private set

    init {
        viewModelScope.launch {
            termUiState = TermUiState(termInfo = termDao.getTermStream(termId).filterNotNull().first().toTermInfo())
        }
    }

    fun updateUiState(termInfo: TermInfo) {
        termUiState = TermUiState(termInfo)
    }

    suspend fun updateTerm() {
        termDao.update(termUiState.termInfo.toTerm())
    }

    suspend fun deleteTerm() {
        termDao.delete(termUiState.termInfo.toTerm()) // will it behaveCorrectly?
    }

//    suspend fun test() { termDao.insert(Term(name = "test000")) }
}

fun Term.toTermInfo() = TermInfo(
    id = id,
    name = name,
    definition = definition
)