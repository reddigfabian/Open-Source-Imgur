package com.fret.imgur_api.impl

import com.fret.di.AppScope
import com.fret.di.SingleIn
import com.fret.imgur_api.api.ImgurRepository
import com.fret.imgur_api.api.models.params.Section
import com.fret.imgur_api.api.models.params.Sort
import com.fret.imgur_api.api.models.params.Window
import com.fret.imgur_api.api.responses.account.AccountResponse
import com.fret.imgur_api.api.responses.album.AlbumImagesResponse
import com.fret.imgur_api.api.responses.api.APICreditsResponse
import com.fret.imgur_api.api.responses.gallery.GalleryListResponse
import com.fret.imgur_api.api.responses.image.ImageListResponse
import com.squareup.anvil.annotations.ContributesBinding
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class ImgurRepositoryImpl @Inject constructor(
    private val imgurService: ImgurService
): ImgurRepository {

    private val clientID = "Client-ID ${BuildConfig.IMGUR_CLIENT_ID}"

    override suspend fun getApiCredits(): APICreditsResponse {
        return imgurService.getApiCredits(clientID)
    }

    override suspend fun getGallery(
        section : Section,
        sort : Sort,
        page : Int,
        window : Window,
        showViral : Boolean,
        showMature : Boolean,
        albumPreviews : Boolean
    ): GalleryListResponse {
        return imgurService.getGallery(
            clientID,
            section,
            sort,
            page,
            window,
            showViral,
            showMature,
            albumPreviews
        )
    }

    override suspend fun getAlbumImages(albumHash: String) : AlbumImagesResponse {
        return imgurService.getAlbumImages(
            clientID,
            albumHash
        )
    }

    override suspend fun getMyAccountImages(accessToken: String): ImageListResponse {
        return imgurService.getMyAccountImages("Bearer $accessToken")
    }

    override suspend fun getAccountForUsername(userName: String) : AccountResponse {
        return imgurService.getAccountForUsername(
            clientID,
            userName
        )
    }

}