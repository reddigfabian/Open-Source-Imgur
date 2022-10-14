package com.fret.imgur_gallery.impl.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.di.AppScope
import com.fret.di.ContributesViewModel
import com.fret.di.SingleIn
import com.fret.imgur_api.api.models.params.Section
import com.fret.imgur_api.api.models.params.Sort
import com.fret.imgur_gallery.impl.di.GalleryScope
import com.fret.imgur_gallery.impl.items.GalleryItem
import com.fret.imgur_gallery.impl.paging.GalleryPagingSource
import com.fret.imgur_gallery.impl.usf.GalleryEffect
import com.fret.imgur_gallery.impl.usf.GalleryEvent
import com.fret.imgur_gallery.impl.usf.GalleryResult
import com.fret.imgur_gallery.impl.usf.GalleryViewState
import com.fret.shared_menus.account.AccountMenuDelegate
import com.fret.shared_menus.account.DefaultAccountMenuDelegate
import com.fret.usf.UsfEvent
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesTo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ListViewModel"

//@ContributesViewModel(GalleryScope::class)
class GalleryViewModel internal constructor(
    private val galleryListPagingSourceFactory: GalleryPagingSource.Factory,
    defaultAccountMenuDelegate: DefaultAccountMenuDelegate
) : ViewModel(), AccountMenuDelegate by defaultAccountMenuDelegate {

    companion object {
        private const val PAGE_SIZE = 60 //Imgur's api doesn't support setting a page size and so we just set this number based on the amount of data the api seems to return.
    }

    private var section = Section.hot
    private var sort = Sort.viral

    private val imgurPagingSourceFactory = InvalidatingPagingSourceFactory {
        galleryListPagingSourceFactory.create(
            section = section,
            sort = sort
        )
    }

    private val pagingLoadEvents = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true
        ), pagingSourceFactory = imgurPagingSourceFactory
    ).flow
        .map { pagingData ->
            pagingData.map { galleryItemModel ->
                GalleryItem(
                    galleryItemModel.id,
                    galleryItemModel.title,
                    "https://i.imgur.com/${galleryItemModel.cover}.jpeg",// TODO:
                    galleryItemModel.score,
                    galleryItemModel.comment_count,
                    galleryItemModel.views
                )
            }
        }
        .cachedIn(viewModelScope)
        .map {
            GalleryEvent.GalleryListItemPageLoadEvent(it)
        }

    private val processEvents = MutableSharedFlow<UsfEvent>()
    private val events = merge(processEvents, pagingLoadEvents)
    private val results = events.mapNotNull {event ->
        Log.d(TAG, "result: $event")
        when (event) {
            GalleryEvent.ScreenLoadEvent -> onScreenLoad()
            is GalleryEvent.GalleryListItemClickedEvent -> onGalleryListItemClickedResult(event.albumHash)
            is GalleryEvent.GalleryListItemPageLoadEvent -> onGalleryPageLoadResult(event.pagingData)
            else -> null
        }
    }

    val viewState = results.scan(GalleryViewState()) { previousState, result ->
        Log.d(TAG, "viewState: $result")
        when (result) {
            is GalleryResult.GalleryListPageLoadResult -> previousState.copy(galleryListPagingData = result.pagingData)
            else -> previousState
        }
    }.distinctUntilChanged()

    val effects = merge(results.mapNotNull { result ->
        when (result) {
            is GalleryResult.GalleryListItemClickedResult -> {
                GalleryEffect.GalleryListItemClicked(result.albumHash)
            }
            else -> null
        }
    },
        accountMenuEffects
    )

    fun processEvent(event: UsfEvent) {
        viewModelScope.launch {
            processEvents.emit(event)
        }
    }

    private fun onScreenLoad(): GalleryResult.ScreenLoadResult = GalleryResult.ScreenLoadResult
    private fun onGalleryListItemClickedResult(albumHash: String): GalleryResult.GalleryListItemClickedResult = GalleryResult.GalleryListItemClickedResult(albumHash)
    private fun onGalleryPageLoadResult(pagingData: PagingData<GalleryItem>): GalleryResult.GalleryListPageLoadResult = GalleryResult.GalleryListPageLoadResult(pagingData)

//    fun test() {
//        imgurAuthState.performActionWithFreshTokens(imgurKtAuthService, object : AuthState.AuthStateAction {
//            override fun execute(
//                accessToken: String?,
//                idToken: String?,
//                ex: AuthorizationException?
//            ) {
//                if (ex != null) {
//                    Log.d(TAG, "exception: $ex")
//                    return
//                } else {
//                    accessToken?.let {
//                        viewModelScope.launch {
//                            val myAccountImages = imgurRepository.getMyAccountImages(it)
//                            Log.d(TAG, "got myAccountImages: ${myAccountImages.data.size}")
//                        }
//                    }
//                }
//            }
//        })
//    }
}