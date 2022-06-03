package com.fret.grocerydemo.kroger_api.responses.product

import com.fret.grocerydemo.kroger_api.models.meta.MetaModel
import com.fret.grocerydemo.kroger_api.models.product.ProductModel

data class KrogerProductResponse(
    val data : List<ProductModel>,
    val meta : MetaModel
)