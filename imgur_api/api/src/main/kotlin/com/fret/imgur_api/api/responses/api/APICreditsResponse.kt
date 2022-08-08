package com.fret.imgur_api.api.responses.api

import com.fret.imgur_api.api.models.api.APICreditsData

data class APICreditsResponse(
    val data : APICreditsData,
    val success : Boolean,
    val status : Int
)