package com.fret.grocerydemo.kroger_api.responses.oauth2

data class AccessTokenResponse(val expires_in : Int, val access_token : String, val token_type : String, val refresh_token : String?)