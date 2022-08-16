package com.fret.gallery_list.impl.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.di.AppScope
import com.fret.di.ContributesViewModel
import com.fret.gallery_list.impl.items.GalleryListItem
import com.fret.gallery_list.impl.paging.GalleryListPagingSource
import com.fret.gallery_list.impl.usf.GalleryListEvent
import com.fret.gallery_list.impl.usf.GalleryListResult
import com.fret.gallery_list.impl.usf.GalleryListViewState
import com.fret.gallery_list.impl.usf.GalleryViewEffect
import com.fret.imgur_api.api.ImgurRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService

private const val TAG = "ListViewModel"

@ContributesViewModel(AppScope::class)
class GalleryListViewModel @AssistedInject constructor(
    @Assisted val args: String,
    private val imgurRepository: ImgurRepository,
    private val imgurAuthState: AuthState,
    private val imgurKtAuthService: AuthorizationService
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    private val imgurPagingSourceFactory = InvalidatingPagingSourceFactory {
        GalleryListPagingSource(PAGE_SIZE, imgurRepository)
    }

    private val galleryListItems: Flow<GalleryListEvent.GalleryListItemPageLoadEvent> = Pager(
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
        .map { GalleryListEvent.GalleryListItemPageLoadEvent(it) }


    private val events = MutableSharedFlow<GalleryListEvent>()
    private val results = merge(events, galleryListItems).map {
        when (it) {
            GalleryListEvent.ScreenLoadEvent -> onScreenLoad()
            is GalleryListEvent.GalleryListItemClickedEvent -> onGalleryListItemClickedResult(it.albumHash)
            is GalleryListEvent.GalleryListItemPageLoadEvent -> onGalleryPageLoadResult(it.pagingData)
            GalleryListEvent.AccountMenuClickedEvent -> onAccountMenuClicked()
        }
    }

    val viewState = results.scan(GalleryListViewState()) { prevState, result ->
        when (result) {
            is GalleryListResult.GalleryListPageLoadResult -> prevState.copy(galleryListPagingData = result.pagingData)
            else -> prevState
        }
    }.distinctUntilChanged()

    val viewEffects = results.mapNotNull { result ->
        when (result) {
            is GalleryListResult.GalleryListItemClickedResult -> {
                GalleryViewEffect.GalleryListItemClicked(result.albumHash)
            }
            GalleryListResult.AccountMenuClickedResult -> {
                GalleryViewEffect.AccountMenuClickedEffect
            }
            else -> null
        }
    }

    fun processEvent(event: GalleryListEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    private fun onScreenLoad(): GalleryListResult.ScreenLoadResult = GalleryListResult.ScreenLoadResult

    private fun onGalleryListItemClickedResult(albumHash: String): GalleryListResult.GalleryListItemClickedResult = GalleryListResult.GalleryListItemClickedResult(albumHash)

    private fun onGalleryPageLoadResult(pagingData: PagingData<GalleryListItem>): GalleryListResult.GalleryListPageLoadResult = GalleryListResult.GalleryListPageLoadResult(pagingData)

    private fun onAccountMenuClicked(): GalleryListResult.AccountMenuClickedResult = GalleryListResult.AccountMenuClickedResult

    fun test() {
        imgurAuthState.performActionWithFreshTokens(imgurKtAuthService, object : AuthState.AuthStateAction {
            override fun execute(
                accessToken: String?,
                idToken: String?,
                ex: AuthorizationException?
            ) {
                if (ex != null) {
                    Log.d(TAG, "exception: $ex")
                    return
                } else {
                    accessToken?.let {
                        viewModelScope.launch {
                            val myAccountImages = imgurRepository.getMyAccountImages(it)
                            Log.d(TAG, "got myAccountImages: ${myAccountImages.data.size}")
                        }
                    }
                }
            }

        })
    }
}