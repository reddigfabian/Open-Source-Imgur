package com.fret.grocerydemo.kroger_api

import android.util.Base64
import com.fret.grocerydemo.BuildConfig
import com.fret.grocerydemo.kroger_api.api_params.KrogerApiScope
import com.fret.grocerydemo.kroger_api.requests.AccessTokenRequest
import com.fret.grocerydemo.kroger_api.responses.AccessTokenResponse
import com.fret.grocerydemo.kroger_api.responses.KrogerProductResponse
import com.fret.grocerydemo.kroger_api.responses.KrogerProductResponseImpl

class KrogerRepositoryImpl(private val krogerService: KrogerService) : KrogerRepository {

    override suspend fun getAuthCode(scope: KrogerApiScope, clientID: String, redirectUri: String) {
        return krogerService.getAuthCode(scope.scopeString, clientID, redirectUri)
    }

    override suspend fun getAccessCode(request : AccessTokenRequest) : AccessTokenResponse {
        return krogerService.getAccessTokenFormUrlEncoded(
            "Basic ${Base64.encodeToString("${BuildConfig.KROGER_CLIENT_ID}:${BuildConfig.KROGER_CLIENT_SECRET}".toByteArray(), Base64.NO_WRAP)}",
            request.grantType.name,
            (request as? AccessTokenRequest.ClientCredentialsAccessTokenRequest)?.scope,
            (request as? AccessTokenRequest.AuthCodeAccessTokenRequest)?.code,
            (request as? AccessTokenRequest.AuthCodeAccessTokenRequest)?.redirectUri,
            (request as? AccessTokenRequest.RefreshTokenAccessTokenRequest)?.refreshToken,
        )
    }

    override fun getItems(pageSize: Int, page : Int): KrogerProductResponse {
        return KrogerProductResponseImpl(DataFaker.generateFakeData(pageSize, page))
    }
}