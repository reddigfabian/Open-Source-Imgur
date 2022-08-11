package com.fret.utils

import androidx.lifecycle.ViewModel

interface ViewModelFactory<VM : ViewModel, S> {
    fun create(initialState: S): VM
}