package com.fret.gallery_list.impl.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.di.AppScope
import com.fret.di.ContributesViewModel
import com.fret.gallery_list.impl.items.GalleryListItem
import com.fret.gallery_list.impl.paging.GalleryListPagingSource
import com.fret.gallery_list.impl.usf.GalleryListEffect
import com.fret.gallery_list.impl.usf.GalleryListEvent
import com.fret.gallery_list.impl.usf.GalleryListResult
import com.fret.gallery_list.impl.usf.GalleryListViewState
import com.fret.imgur_api.api.models.params.Section
import com.fret.imgur_api.api.models.params.Sort
import com.fret.shared_menus.account.AccountMenuDelegate
import com.fret.shared_menus.account.DefaultAccountMenuDelegate
import com.fret.shared_menus.account.usf.AccountMenuEffect
import com.fret.shared_menus.account.usf.AccountMenuEvent
import com.fret.usf.UsfEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "ListViewModel"

@ContributesViewModel(AppScope::class)
class GalleryListViewModel @AssistedInject constructor(
    @Assisted val args: String,
    private val galleryListPagingSourceFactory: GalleryListPagingSource.Factory,
    defaultAccountMenuDelegate: DefaultAccountMenuDelegate
) : ViewModel(), AccountMenuDelegate by defaultAccountMenuDelegate {

    companion object {
        private const val PAGE_SIZE = 60 //Imgur's api doesn't support setting a page size and so we just set this number based on the amount of data the api seems to return.
    }

    var section = Section.hot
    var sort = Sort.viral

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
                GalleryListItem(
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
            GalleryListEvent.GalleryListItemPageLoadEvent(it)
        }

    private val processEvents = MutableSharedFlow<UsfEvent>()
    private val events = merge(processEvents, pagingLoadEvents)
    private val results = events.mapNotNull {event ->
        Log.d(TAG, "result: $event")
        when (event) {
            GalleryListEvent.ScreenLoadEvent -> onScreenLoad()
            is GalleryListEvent.GalleryListItemClickedEvent -> onGalleryListItemClickedResult(event.albumHash)
            is GalleryListEvent.GalleryListItemPageLoadEvent -> onGalleryPageLoadResult(event.pagingData)
            else -> null
        }
    }

    val viewState = results.scan(GalleryListViewState()) { previousState, result ->
        Log.d(TAG, "viewState: $result")
        when (result) {
            is GalleryListResult.GalleryListPageLoadResult -> previousState.copy(galleryListPagingData = result.pagingData)
            else -> previousState
        }
    }.distinctUntilChanged()

    val viewEffects = merge(results.mapNotNull { result ->
        when (result) {
            is GalleryListResult.GalleryListItemClickedResult -> {
                GalleryListEffect.GalleryListItemClicked(result.albumHash)
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

    private fun onScreenLoad(): GalleryListResult.ScreenLoadResult = GalleryListResult.ScreenLoadResult
    private fun onGalleryListItemClickedResult(albumHash: String): GalleryListResult.GalleryListItemClickedResult = GalleryListResult.GalleryListItemClickedResult(albumHash)
    private fun onGalleryPageLoadResult(pagingData: PagingData<GalleryListItem>): GalleryListResult.GalleryListPageLoadResult = GalleryListResult.GalleryListPageLoadResult(pagingData)

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