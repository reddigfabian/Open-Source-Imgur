package com.fret.kroger_api.api.repositories

import com.fret.kroger_api.api.responses.KrogerProductResponse

interface KrogerRepository {
    fun getItems(pageSize: Int, page : Int) : KrogerProductResponse
}