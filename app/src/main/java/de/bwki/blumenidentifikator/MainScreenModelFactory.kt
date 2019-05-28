package de.bwki.blumenidentifikator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainScreenModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainScreenModel::class.java)) {
            return MainScreenModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}