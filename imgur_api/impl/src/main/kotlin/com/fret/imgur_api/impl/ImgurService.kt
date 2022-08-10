package com.fret.imgur_api.impl

import com.fret.imgur_api.api.responses.api.APICreditsResponse
import com.fret.imgur_api.api.responses.gallery.GalleryListResponse
import com.fret.imgur_api.api.responses.image.ImageListResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ImgurService {
    @GET("credits")
    suspend fun getApiCredits(
        @Header("Authorization") accessToken : String
    ) : APICreditsResponse

    @GET("gallery/{section}/{sort}/{page}/{window}")
    suspend fun getGallery(
        @Header("Authorization") accessToken : String,
        @Path("sort") window : String = "day",
        @Path("section") section : String = "hot",
        @Path("sort") sort : String = "viral",
        @Path("sort") page : Int = 0,
        @Query("showViral") showViral : Boolean = true,
        @Query("showMature") showMature : Boolean = false,
        @Query("showViral") album_previews : Boolean = true
    ) : GalleryListResponse

    @GET("account/me/images")
    suspend fun getMyAccountImages(
        @Header("Authorization") accessToken : String
    ): ImageListResponse
}