package com.fret.imgur_api.api.models.api

data class APICreditsModel (
    val userLimit : Int,
    val userRemaining : Int,
    val userReset : Int,
    val clientLimit : Int,
    val clientRemaining : Int
)