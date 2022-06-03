package com.fret.grocerydemo.kroger_api

import android.util.Base64
import com.fret.grocerydemo.BuildConfig
import com.fret.grocerydemo.kroger_api.api_params.KrogerApiScope
import com.fret.grocerydemo.kroger_api.requests.oauth2.AccessTokenRequest
import com.fret.grocerydemo.kroger_api.responses.identity.KrogerIdentityResponse
import com.fret.grocerydemo.kroger_api.responses.oauth2.AccessTokenResponse
import com.fret.grocerydemo.kroger_api.responses.product.KrogerProductResponse
import retrofit2.Call

class KrogerRepository(private val krogerService: KrogerService) {

    fun getAuthCode(scope: KrogerApiScope, clientID: String, redirectUri: String) {
        return krogerService.getAuthCode(scope.scopeString, clientID, redirectUri)
    }

    fun getAccessCode(request : AccessTokenRequest) : Call<AccessTokenResponse> {
        return krogerService.getAccessToken(
            "Basic ${Base64.encodeToString("${BuildConfig.KROGER_CLIENT_ID}:${BuildConfig.KROGER_CLIENT_SECRET}".toByteArray(), Base64.NO_WRAP)}",
            request.grantType.name,
            (request as? AccessTokenRequest.ClientCredentialsAccessTokenRequest)?.scope,
            (request as? AccessTokenRequest.AuthCodeAccessTokenRequest)?.code,
            (request as? AccessTokenRequest.AuthCodeAccessTokenRequest)?.redirectUri,
            (request as? AccessTokenRequest.RefreshTokenAccessTokenRequest)?.refreshToken,
        )
    }

    suspend fun getItems(pageSize: Int, page : Int): KrogerProductResponse {
        return krogerService.getProducts(pageSize, page)
    }

    suspend fun getProfileID(accessToken : String): KrogerIdentityResponse {
        return krogerService.getUserID(accessToken)
    }
}