package com.fret.gallery_list.impl

import androidx.lifecycle.ViewModel

interface ViewModelFactoryPlugin<VM : ViewModel> {
    fun create(initialState: String): VM
}