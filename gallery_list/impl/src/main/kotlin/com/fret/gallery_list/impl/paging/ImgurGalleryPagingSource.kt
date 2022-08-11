package com.fret.gallery_list.impl.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fret.imgur_api.api.ImgurRepository
import com.fret.imgur_api.api.models.gallery.GalleryItemModel

class ImgurGalleryPagingSource(
    private val pageSize: Int,
    private val imgurRepository: ImgurRepository
) : PagingSource<Int, GalleryItemModel>() {

    companion object {
        private const val STARTING_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItemModel> {
        return try {
            val pageNumber = params.key ?: STARTING_INDEX
            val response = imgurRepository.getGalleryList(pageNumber)
            val prevKey = if (pageNumber > STARTING_INDEX) pageNumber - 1 else null
            val nextKey = if (response.data.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(
                data = response.data,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GalleryItemModel>): Int {
        return state.anchorPosition?: STARTING_INDEX
    }


}