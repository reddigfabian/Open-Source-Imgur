package com.fret.grocerydemo.kroger_api.api_params

enum class KrogerApiScope(val scopeString : String) {
    PROFILE_COMPACT("profile.compact"),
    PRODUCT_COMPACT("product.compact"),
    CART_BASIC_WRITE("cart.basic:write")
}