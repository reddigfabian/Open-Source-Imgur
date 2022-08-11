package com.fret.gallery_list.impl

import androidx.lifecycle.ViewModel

interface ViewModelFactoryExperimental<VM : ViewModel> {
    fun create(initialState: String): VM
}