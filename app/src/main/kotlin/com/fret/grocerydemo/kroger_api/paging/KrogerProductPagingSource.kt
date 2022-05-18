package com.fret.grocerydemo.kroger_api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fret.grocerydemo.kroger_api.KrogerRepository
import javax.inject.Inject

class KrogerProductPagingSource @Inject constructor(val krogerRepository: KrogerRepository) : PagingSource<Int, String>() {

    companion object {
        const val PAGE_SIZE = 30
        private const val STARTING_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        return try {
            val pageNumber = params.key ?: 0
            val response = krogerRepository.getItems(PAGE_SIZE, pageNumber)
            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            val nextKey = if (response.products.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(
                data = response.products,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, String>): Int {
        return state.anchorPosition?: STARTING_INDEX
    }


}