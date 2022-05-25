package com.fret.grocerydemo.kroger_api

import com.fret.grocerydemo.kroger_api.api_params.KrogerApiScope
import com.fret.grocerydemo.kroger_api.requests.AccessTokenRequest
import com.fret.grocerydemo.kroger_api.responses.AccessTokenResponse
import com.fret.grocerydemo.kroger_api.responses.KrogerProductResponse

interface KrogerRepository {
    suspend fun getAuthCode(scope : KrogerApiScope, clientID : String, redirectUri : String)
    suspend fun getAccessCode(request : AccessTokenRequest) : AccessTokenResponse
    fun getItems(pageSize: Int, page : Int) : KrogerProductResponse
}