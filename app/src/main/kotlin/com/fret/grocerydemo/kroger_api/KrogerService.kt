package com.fret.grocerydemo.kroger_api

import com.fret.grocerydemo.kroger_api.responses.identity.KrogerIdentityResponse
import com.fret.grocerydemo.kroger_api.responses.oauth2.AccessTokenResponse
import com.fret.grocerydemo.kroger_api.responses.product.KrogerProductResponse
import retrofit2.Call
import retrofit2.http.*

interface KrogerService {

    //OAuth2
    @GET("connect/oauth2/authorize")
    fun getAuthCode(
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

    //Identity
    @GET("identity/profile")
    suspend fun getUserID(
        @Header("Authorization") accessToken : String
    ) : KrogerIdentityResponse

    //Products
    @GET("products")
    suspend fun getProducts(
        @Query("filter.limit") limit : Int,
        @Query("filter.start") start : Int,
        @Query("filter.term") filterTerm : String = "kroger"
    ) : KrogerProductResponse
}