package com.fret.grocerydemo.kroger_api

import androidx.paging.PagingSource
import com.fret.grocerydemo.kroger_api.responses.KrogerProductResponse

interface KrogerRepository {
    fun getItems(pageSize: Int, page : Int) : KrogerProductResponse
}