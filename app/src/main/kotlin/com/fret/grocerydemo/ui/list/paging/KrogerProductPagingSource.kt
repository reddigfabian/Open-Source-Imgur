package com.fret.grocerydemo.ui.list.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fret.grocerydemo.kroger_api.KrogerRepository
import com.fret.grocerydemo.kroger_api.models.product.ProductModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class KrogerProductPagingSource(
    private val pageSize: Int,
    private val krogerRepository: KrogerRepository
) : PagingSource<Int, ProductModel>() {

    companion object {
        private const val STARTING_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductModel> {
        return try {
            val pageNumber = params.key ?: STARTING_INDEX
            val response = krogerRepository.getItems(pageSize, pageNumber)
            val prevKey = if (pageNumber > STARTING_INDEX) pageNumber - response.meta.pagination.limit else null
            val nextKey = if (response.data.isNotEmpty()) pageNumber + response.meta.pagination.limit else null

            LoadResult.Page(
                data = response.data,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductModel>): Int {
        return state.anchorPosition?: STARTING_INDEX
    }


}