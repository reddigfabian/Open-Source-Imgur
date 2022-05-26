package com.fret.grocerydemo.kroger_api

import com.fret.grocerydemo.kroger_api.responses.AccessTokenResponse
import com.fret.grocerydemo.kroger_api.responses.KrogerProductResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.*

interface KrogerService {
    @GET("connect/oauth2/authorize")
    suspend fun getAuthCode(
        @Query("scope") scope : String,
        @Query("client_id") clientID : String,
        @Query("redirect_uri", encoded = true) redirectUri : String,
        @Query("response_type") requestType : String = "code"
    )

    @FormUrlEncoded
    @POST("connect/oauth2/token")
    fun getAccessToken(
        @Header("Authorization") base64Auth : String,
        @Field("grant_type") grantType : String,
        @Field("scope") scope : String? = null,
        @Field("code") authCode : String? = null,
        @Field("redirect_uri") redirectUri : String? = null,
        @Field("refresh_token") refreshToken : String? = null
    ) : Call<AccessTokenResponse>

    @GET("products")
    suspend fun getProducts(
        @Query("filter.limit") limit : Int,
        @Query("filter.start") start : Int,
        @Query("filter.term") filterTerm : String = "creamer"
    ) : KrogerProductResponse
}