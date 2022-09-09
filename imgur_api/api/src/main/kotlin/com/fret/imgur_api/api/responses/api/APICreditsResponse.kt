package com.fret.imgur_api.api.responses.api

import com.fret.imgur_api.api.models.api.APICreditsModel

data class APICreditsResponse(
    val data : APICreditsModel,
    val success : Boolean,
    val status : Int
)