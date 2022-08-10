package com.fret.gallery_list.impl

import androidx.lifecycle.ViewModel

interface ViewModelFactoryPlugin<VM : ViewModel, S : String> {
    fun create(initialState: S): VM
}