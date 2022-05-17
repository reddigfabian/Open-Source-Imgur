package com.fret.grocerydemo.kroger_api

import androidx.paging.PagingSource
import com.fret.grocerydemo.kroger_api.paging.KrogerProductPagingSource
import com.fret.grocerydemo.kroger_api.responses.KrogerProductResponse
import com.fret.grocerydemo.kroger_api.responses.KrogerProductResponseImpl

class KrogerRepositoryImpl : KrogerRepository {
    override fun getItems(pageSize: Int, page : Int): KrogerProductResponse {
        return KrogerProductResponseImpl(generateFakeData(pageSize, page))
    }

    override fun getItemsPagingSource(pageSize: Int): PagingSource<Int, String> {
        return KrogerProductPagingSource(pageSize)
    }

    private fun generateFakeData(pageSize : Int, page : Int) : List<String> {
        val result = mutableListOf<String>()
        var indexCopy = (page * pageSize) + 1
        repeat(pageSize) {
            result.add("Kroger Item $indexCopy")
            indexCopy++
        }
        return result
    }
}