package com.fret.gallery_list.impl

import com.fret.gallery_list.impl.viewmodels.GalleryListViewModel
import com.fret.utils.di.ViewModelFactoryExperimental

interface GalleryListViewModelFactory:
    com.fret.utils.di.ViewModelFactoryExperimental<GalleryListViewModel> {
    fun createGalleryListViewModel(args: String): GalleryListViewModel {
        return create(args)
    }
}