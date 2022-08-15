package com.fret.gallery_list.impl.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.di.AppScope
import com.fret.di.ContributesViewModel
import com.fret.gallery_list.impl.items.GalleryListItem
import com.fret.gallery_list.impl.paging.GalleryListPagingSource
import com.fret.imgur_api.api.ImgurRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
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

    val imgurItems: Flow<PagingData<GalleryListItem>> = Pager(
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
                    "https://i.imgur.com/${galleryItemModel.cover}.jpeg",
                    galleryItemModel.score,
                    galleryItemModel.comment_count,
                    galleryItemModel.views
                )
            }
        }
        .cachedIn(viewModelScope)
//
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
//                        }
//                    }
//                }
//            }
//
//        })
//    }
}