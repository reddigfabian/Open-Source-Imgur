package com.fret.gallery_list.impl.usf

import androidx.paging.PagingData
import com.fret.gallery_list.impl.items.GalleryListItem
import com.fret.usf.UsfResult

sealed class GalleryListResult: UsfResult {
    object ScreenLoadResult: GalleryListResult()
    data class GalleryListItemClickedResult(val albumHash: String): GalleryListResult()
    data class GalleryListPageLoadResult(val pagingData: PagingData<GalleryListItem>): GalleryListResult()
}