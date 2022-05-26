package com.fret.grocerydemo.kroger_api.models.product

data class ProductImageModel (
    val perspective : String,
    val featured : Boolean = false,
    val sizes : List<ProductImageSizeModel>
)