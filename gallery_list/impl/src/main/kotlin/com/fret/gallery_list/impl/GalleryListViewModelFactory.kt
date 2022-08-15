package com.fret.gallery_list.impl

import com.fret.gallery_list.impl.viewmodels.GalleryListViewModel
import com.fret.utils.ViewModelFactoryExperimental

interface GalleryListViewModelFactory: ViewModelFactoryExperimental<GalleryListViewModel> {
    fun createGalleryListViewModel(args: String): GalleryListViewModel {
        return create(args)
    }
}