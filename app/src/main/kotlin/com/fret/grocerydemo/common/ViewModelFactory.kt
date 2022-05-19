package com.fret.grocerydemo.common

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory<VM : ViewModel> @Inject constructor(private val provider: Provider<VM>) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = provider.get() as T
}

interface StateViewModelFactory<VM : ViewModel> {
    fun create(handle: SavedStateHandle): VM
}