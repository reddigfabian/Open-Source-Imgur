package com.fret.grocerydemo.kroger_api.requests

import com.squareup.moshi.Json

sealed class AccessTokenRequest(@Json(name="grant_type") val grantType : GrantType) {
    data class ClientCredentialsAccessTokenRequest(val scope : String = "product.compact") : AccessTokenRequest(GrantType.client_credentials)
    data class AuthCodeAccessTokenRequest(val code : String, val redirectUri : String) : AccessTokenRequest(GrantType.authorization_code)
    data class RefreshTokenAccessTokenRequest(val refreshToken : String) : AccessTokenRequest(GrantType.refresh_token)

    enum class GrantType {
        client_credentials,
        authorization_code,
        refresh_token
    }
}