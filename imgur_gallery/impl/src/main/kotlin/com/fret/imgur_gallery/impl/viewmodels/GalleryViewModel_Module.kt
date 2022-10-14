package com.fret.imgur_gallery.impl.viewmodels

import androidx.lifecycle.ViewModelProvider
import com.fret.imgur_gallery.impl.di.GalleryScope
import com.fret.utils.di.ViewModelKey
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ContributesTo(GalleryScope::class)
abstract class GalleryViewModel_Module {

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    abstract fun bindGalleryViewModelFactory(factory: GalleryViewModel_Factory): ViewModelProvider.Factory

}