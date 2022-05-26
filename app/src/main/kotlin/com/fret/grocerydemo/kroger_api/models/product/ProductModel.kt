package com.fret.grocerydemo.kroger_api.models.product

data class ProductModel(
    val productId : String,
    val aisleLocations : List<ProductAisleLocationModel>,
    val brand : String,
    val categories : List<String>,
    val countryOrigin : String?,
    val description : String,
    val items : List<ProductItemModel>,
    val itemInformation : ProductBoxedDimensionsModel,
    val temperature : ProductTemperatureModel,
    val images : List<ProductImageModel>,
    val upc : String
)