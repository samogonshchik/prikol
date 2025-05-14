package com.example.prikol

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.prikol.ui.viewModels.AddTermViewModel
import com.example.prikol.ui.viewModels.EditTermViewModel
import com.example.prikol.ui.viewModels.HomeViewModel
import com.example.prikol.ui.viewModels.ViewTermViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                prikolApplication().container.termDao
            )
        }
        initializer {
            AddTermViewModel(
                prikolApplication().container.termDao
            )
        }
        initializer {
            ViewTermViewModel(
                this.createSavedStateHandle(),
                prikolApplication().container.termDao
            )
        }
        initializer {
            EditTermViewModel(
                this.createSavedStateHandle(),
                prikolApplication().container.termDao
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [PrikolApplication].
 */

fun CreationExtras.prikolApplication(): PrikolApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PrikolApplication)
