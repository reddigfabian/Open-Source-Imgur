package com.fret.imgur_gallery.impl.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fret.imgur_gallery.impl.paging.GalleryPagingSource
import com.fret.shared_menus.account.DefaultAccountMenuDelegate
import javax.inject.Inject

class GalleryViewModel_Factory @Inject constructor(
    private val galleryListPagingSourceFactory: GalleryPagingSource.Factory,
    private val defaultAccountMenuDelegate: DefaultAccountMenuDelegate
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(galleryListPagingSourceFactory, defaultAccountMenuDelegate) as T
    }
}
