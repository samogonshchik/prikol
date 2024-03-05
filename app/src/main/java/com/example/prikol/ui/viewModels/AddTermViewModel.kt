package com.example.prikol.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.prikol.data.Term
import com.example.prikol.data.TermDao

class AddTermViewModel(
    private val termDao: TermDao
) : ViewModel() {
    var termUiState by mutableStateOf(TermUiState())
        private set

    fun updateUiState(termInfo: TermInfo) {
        termUiState = TermUiState(termInfo)
    }

    suspend fun saveTerm() {
        if (isEnterCorrect()) {
            termDao.insert(termUiState.termInfo.toTerm())
            termUiState = TermUiState()
        }
    }

    private fun isEnterCorrect(): Boolean {
        termUiState.termInfo.let {
            if (it.name == "" || it.definition == "") return false
        }
        return true
    }
}

data class TermUiState(
    val termInfo: TermInfo = TermInfo()
)

data class TermInfo(
    val id: Int = 0,
    val type: String = "Term",
    val name: String = "",
    val definition: String = ""
)

fun TermInfo.toTerm() = Term(
    id = id,
    type = type,
    name = name,
    definition = definition
)