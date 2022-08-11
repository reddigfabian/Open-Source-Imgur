package com.fret.gallery_list.impl

import androidx.lifecycle.ViewModel

interface ViewModelFactory<VM : ViewModel, S: String> {
    fun create(initialState: S): VM
}