package com.fret.grocerydemo.kroger_api

import com.fret.grocerydemo.kroger_api.responses.AccessTokenResponse
import retrofit2.http.*

interface KrogerService {
    @GET("connect/oauth2/authorize")
    suspend fun getAuthCode(@Query("scope") scope : String, @Query("client_id") clientID : String, @Query("redirect_uri", encoded = true) redirectUri : String, @Query("response_type") requestType : String = "code")

    @FormUrlEncoded
    @POST("connect/oauth2/token")
    suspend fun getAccessTokenFormUrlEncoded(
        @Header("Authorization") base64Auth : String,
        @Field("grant_type") grantType : String,
        @Field("scope") scope : String? = null,
        @Field("code") code : String? = null,
        @Field("redirect_uri") redirectUri : String? = null,
        @Field("refresh_token") refreshToken : String? = null
    ) : AccessTokenResponse
}
