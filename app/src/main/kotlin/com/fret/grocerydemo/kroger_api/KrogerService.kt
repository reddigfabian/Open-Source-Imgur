package com.fret.grocerydemo.kroger_api

import com.fret.grocerydemo.kroger_api.requests.ClientCredentialsOAuthRequest
import com.fret.grocerydemo.kroger_api.responses.AccessTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface KrogerService {
    @POST("connect/oauth2/token")
    suspend fun getAccessToken(@Body accessTokenRequest : ClientCredentialsOAuthRequest) : AccessTokenResponse
}
