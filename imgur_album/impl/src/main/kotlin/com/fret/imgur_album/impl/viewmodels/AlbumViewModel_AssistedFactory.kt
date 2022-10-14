package com.fret.imgur_album.impl.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fret.imgur_api.api.ImgurRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@AssistedFactory
interface AlbumViewModel_Factory_AssistedFactory {
    fun create(albumHash: String): AlbumViewModel_MyFactory
}

class AlbumViewModel_MyFactory @AssistedInject constructor(
    @Assisted private val albumHash: String,
    private val imgurRepository: ImgurRepository
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlbumViewModel(albumHash, imgurRepository) as T
    }
}
