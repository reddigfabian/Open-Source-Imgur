package com.fret.imgur_album.impl.viewmodels

import com.fret.imgur_album.impl.di.AlbumScope
import com.fret.utils.di.ViewModelKey
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ContributesTo(AlbumScope::class)
abstract class AlbumViewModel_Module {

    @Binds
    @IntoMap
    @ViewModelKey(AlbumViewModel::class)
    abstract fun bindAlbumViewModelFactory(factory: AlbumViewModel_Factory_AssistedFactory): AlbumViewModel_Factory_AssistedFactory

}