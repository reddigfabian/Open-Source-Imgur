package com.fret.imgur_api.api

import com.fret.imgur_api.api.responses.api.APICreditsResponse
import com.fret.imgur_api.api.responses.gallery.GalleryListResponse
import com.fret.imgur_api.api.responses.image.ImageListResponse

interface ImgurRepository {
    suspend fun getApiCredits(): APICreditsResponse

    suspend fun getGalleryList(pageNumber: Int) : GalleryListResponse

    suspend fun getMyAccountImages(accessToken: String): ImageListResponse
}