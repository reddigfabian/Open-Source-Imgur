package com.fret.gallery_list.impl.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fret.di.AppScope
import com.fret.gallery_list.impl.di.GalleryListScope
import com.fret.gallery_list.impl.viewholders.GalleryListItemViewHolder
import com.fret.imgur_api.api.ImgurRepository
import com.fret.imgur_api.api.models.gallery.GalleryItemModel
import com.fret.imgur_api.api.models.params.Section
import com.fret.imgur_api.api.models.params.Sort
import com.fret.imgur_api.api.models.params.Window
import com.squareup.anvil.annotations.ContributesBinding
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

abstract class GalleryListPagingSource: PagingSource<Int, GalleryItemModel>() {
    abstract val section: Section
    abstract val sort: Sort
    abstract val window: Window

    interface Factory {
        fun create(section: Section = Section.hot, sort: Sort = Sort.viral, window: Window = Window.day): GalleryListPagingSource
    }
}

// TODO: Add more params for gallery api call to this paging source (sort, section, window, etc.)
class GalleryListPagingSourceImpl @AssistedInject constructor(
    @Assisted override val section: Section,
    @Assisted override val sort: Sort,
    @Assisted override val window: Window,
    private val imgurRepository: ImgurRepository
): GalleryListPagingSource() {

    companion object {
        private const val STARTING_INDEX = 0
    }

    @AssistedFactory
    @ContributesBinding(AppScope::class)
    interface Factory : GalleryListPagingSource.Factory {
        override fun create(section: Section, sort: Sort, window: Window): GalleryListPagingSourceImpl
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItemModel> {
        return try {
            val pageNumber = params.key ?: STARTING_INDEX
            val response = imgurRepository.getGallery(
                section = section,
                sort = sort,
                page = pageNumber,
                window = window
            )
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