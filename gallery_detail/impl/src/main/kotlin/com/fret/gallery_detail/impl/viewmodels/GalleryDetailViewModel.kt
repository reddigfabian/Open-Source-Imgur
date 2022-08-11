package com.fret.gallery_detail.impl.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fret.di.AppScope
import com.fret.di.ContributesViewModel
import com.fret.gallery_detail.impl.items.GalleryDetailListItem
import com.fret.imgur_api.api.ImgurRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@ContributesViewModel(AppScope::class)
class GalleryDetailViewModel @AssistedInject constructor(
    @Assisted private val albumHash: String,
    private val imgurRepository: ImgurRepository
) : ViewModel() {

    private val _images = MutableStateFlow<List<GalleryDetailListItem>>(emptyList())
    val images : StateFlow<List<GalleryDetailListItem>> get() = _images

    init {
        loadAlbumImages()
    }

    private fun loadAlbumImages() {
        viewModelScope.launch {
            _images.update {
                imgurRepository.getAlbumImages(albumHash).data.map {
                    GalleryDetailListItem(
                        it.id,
                        it.link,
                        it.description
                    )
                }
            }
        }
    }
}