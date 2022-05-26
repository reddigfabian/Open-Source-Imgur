package com.fret.grocerydemo.kroger_api.models.meta

data class MetaModel(
    val pagination : Pagination,
    val warnings : List<String>?
)