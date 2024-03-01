package com.example.prikol.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prikol.data.Term
import com.example.prikol.data.TermDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val termDao: TermDao) : ViewModel() { // If no function use it, "private val" isn't needed

    val homeUiState: StateFlow<HomeUiState> = termDao.getAllTermsStream().map { HomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeUiState()
        )

    suspend fun deleteAll() {
        termDao.deleteAll()
    }

    suspend fun testAppend() {
        termDao.insert(Term(name = "placeholder"))
    }
}

data class HomeUiState(
    val termsList: List<Term> = listOf()
)