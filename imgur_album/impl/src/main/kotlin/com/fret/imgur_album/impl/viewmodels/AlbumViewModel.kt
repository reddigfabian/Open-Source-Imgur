package com.fret.imgur_album.impl.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fret.di.AppScope
import com.fret.di.ContributesViewModel
import com.fret.imgur_album.impl.di.AlbumScope
import com.fret.imgur_album.impl.items.ImageListItem
import com.fret.imgur_album.impl.usf.AlbumEvent
import com.fret.imgur_album.impl.usf.AlbumResult
import com.fret.imgur_album.impl.usf.AlbumViewEffect
import com.fret.imgur_album.impl.usf.AlbumViewState
import com.fret.imgur_api.api.ImgurRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

//@ContributesViewModel(AppScope::class)
class AlbumViewModel @AssistedInject internal constructor(
    @Assisted private val albumHash: String,
    private val imgurRepository: ImgurRepository
) : ViewModel() {

    private val events = MutableSharedFlow<AlbumEvent>()
    private val results = events.map {
        when (it) {
            AlbumEvent.ScreenLoadEvent -> onScreenLoad()
            AlbumEvent.AccountMenuClickedEvent -> onAccountMenuClicked()
        }
    }

    val viewState = results.scan(AlbumViewState()) { prevState, result ->
        when (result) {
            is AlbumResult.ScreenLoadResult -> {
                prevState.copy(
                    errorMessage = result.error,
                    imageListItems = result.imageList
                )
            }
            else -> prevState
        }
    }.distinctUntilChanged()

    val viewEffects = results.mapNotNull { result ->
        when (result) {
            AlbumResult.AccountMenuClickedResult -> {
                AlbumViewEffect.AccountMenuClickedEffect
            }
            else -> null
        }
    }

    fun processEvent(event: AlbumEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    private suspend fun onScreenLoad(): AlbumResult.ScreenLoadResult {
        return try {
            AlbumResult.ScreenLoadResult(imageList = loadAlbumImages())
        } catch (ex: Exception) {
            AlbumResult.ScreenLoadResult(error = ex.message)
        }
    }

    private fun onAccountMenuClicked(): AlbumResult.AccountMenuClickedResult = AlbumResult.AccountMenuClickedResult

    private suspend fun loadAlbumImages(): List<ImageListItem> {
        return imgurRepository.getAlbumImages(albumHash).data.map {
            ImageListItem(
                it.id,
                it.type,
                it.link,
                it.description
            )
        }
    }
}