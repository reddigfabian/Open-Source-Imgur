package com.fret.utils.di

import androidx.lifecycle.ViewModel

interface ViewModelFactory<VM : ViewModel, S: Any?> {
    fun create(args: S): VM
}