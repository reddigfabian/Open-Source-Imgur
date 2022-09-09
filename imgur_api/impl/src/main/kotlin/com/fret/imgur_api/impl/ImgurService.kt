package com.fret.imgur_api.impl

import com.fret.imgur_api.api.models.params.Section
import com.fret.imgur_api.api.models.params.Sort
import com.fret.imgur_api.api.models.params.Window
import com.fret.imgur_api.api.responses.account.AccountResponse
import com.fret.imgur_api.api.responses.album.AlbumImagesResponse
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
        @Header("Authorization") clientID : String,
        @Path("section") section : Section = Section.hot,
        @Path("sort") sort : Sort = Sort.viral,
        @Path("page") page : Int = 0,
        @Path("window") window : Window = Window.day,
        @Query("showViral") showViral : Boolean = true,
        @Query("mature") showMature : Boolean = false,
        @Query("album_previews") albumPreviews : Boolean = false
    ) : GalleryListResponse

    @GET("album/{albumHash}/images")
    suspend fun getAlbumImages(
        @Header("Authorization") clientID : String,
        @Path("albumHash") albumHash : String
    ) : AlbumImagesResponse

    @GET("account/me/images")
    suspend fun getMyAccountImages(
        @Header("Authorization") accessToken : String
    ): ImageListResponse

    @GET("account/{username}")
    suspend fun getAccountForUsername(
        @Header("Authorization") clientID : String,
        @Path("username") username : String
    ): AccountResponse
}