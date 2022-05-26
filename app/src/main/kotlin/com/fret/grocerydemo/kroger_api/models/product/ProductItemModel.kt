package com.fret.grocerydemo.kroger_api.models.product

data class ProductItemModel(
    val itemId : String,
    val favorite : Boolean,
    val size : String,
    val fulfillment : FulfillmentModel
)