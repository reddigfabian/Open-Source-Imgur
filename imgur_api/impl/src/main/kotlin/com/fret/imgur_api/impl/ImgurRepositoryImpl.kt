package com.fret.imgur_api.impl

import com.fret.di.AppScope
import com.fret.di.SingleIn
import com.fret.imgur_api.api.ImgurRepository
import com.fret.imgur_api.api.responses.album.AlbumImagesResponse
import com.fret.imgur_api.api.responses.api.APICreditsResponse
import com.fret.imgur_api.api.responses.gallery.GalleryListResponse
import com.fret.imgur_api.api.responses.image.ImageListResponse
import com.squareup.anvil.annotations.ContributesBinding
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

    override suspend fun getGallery(pageNumber: Int): GalleryListResponse {
        return imgurService.getGallery(clientID = clientID, page = pageNumber)
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

}