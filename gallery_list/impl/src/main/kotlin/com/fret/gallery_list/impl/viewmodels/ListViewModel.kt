package com.fret.gallery_list.impl.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.di.AppScope
import com.fret.di.ContributesViewModel
import com.fret.gallery_list.impl.items.ImgurListItem
import com.fret.gallery_list.impl.paging.ImgurGalleryPagingSource
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
class ListViewModel @AssistedInject constructor(
//    val savedStateHandle: SavedStateHandle,
    @Assisted val initialState: String,
    val imgurRepository: ImgurRepository,
    val imgurAuthState: AuthState,
    val imgurKtAuthService: AuthorizationService
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    private val imgurPagingSourceFactory = InvalidatingPagingSourceFactory {
        ImgurGalleryPagingSource(PAGE_SIZE, imgurRepository)
    }

    val toast = MutableSharedFlow<String>()

    val imgurItems: Flow<PagingData<ImgurListItem>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true
        ), pagingSourceFactory = imgurPagingSourceFactory
    ).flow
        .map { pagingData ->
            pagingData.map { galleryItemModel ->
                ImgurListItem(galleryItemModel.title, "https://i.imgur.com/${galleryItemModel.cover}.jpeg")
            }
        }
        .cachedIn(viewModelScope)


    fun test() {
        imgurAuthState.performActionWithFreshTokens(imgurKtAuthService, object : AuthState.AuthStateAction {
            override fun execute(
                accessToken: String?,
                idToken: String?,
                ex: AuthorizationException?
            ) {
                if (ex != null) {
                    Log.d(TAG, "exception: $ex")
//                    Toast.makeText(requireContext(), "DID A FAT OOPS!", Toast.LENGTH_SHORT).show()
                    return
                } else {
                    accessToken?.let {
                        viewModelScope.launch {
                            val myAccountImages = imgurRepository.getMyAccountImages(it)
                            toast.emit(myAccountImages.data.size.toString())
                        }
                    }
                }
            }

        })
    }
}