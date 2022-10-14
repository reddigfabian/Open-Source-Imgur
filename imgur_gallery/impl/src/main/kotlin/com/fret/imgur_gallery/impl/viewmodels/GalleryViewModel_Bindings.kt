package com.fret.imgur_gallery.impl.viewmodels

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fret.imgur_gallery.impl.di.GalleryScope
import com.fret.utils.di.bindings
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(GalleryScope::class)
interface GalleryViewModel_Bindings {
    fun viewModelFactories(): Map<Class<out ViewModel>, ViewModelProvider.Factory>
}

internal fun Fragment.viewModelFactory(): Lazy<GalleryViewModel> = viewModels {
    (bindings<GalleryViewModel_Bindings>().viewModelFactories()[GalleryViewModel::class.java] as GalleryViewModel_Factory)
}