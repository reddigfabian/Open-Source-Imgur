package com.fret.grocerydemo.kroger_api.models.product

data class FulfillmentModel(
    val curbside : Boolean,
    val delivery : Boolean,
    val inStore : Boolean,
    val shipToHome : Boolean
)