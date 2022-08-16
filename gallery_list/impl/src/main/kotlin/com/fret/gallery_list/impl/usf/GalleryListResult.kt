package com.fret.gallery_list.impl.usf

import androidx.paging.PagingData
import com.fret.gallery_list.impl.items.GalleryListItem

sealed class GalleryListResult {
    object ScreenLoadResult: GalleryListResult()
    data class GalleryListItemClickedResult(val albumHash: String): GalleryListResult()
    data class GalleryListPageLoadResult(val pagingData: PagingData<GalleryListItem>): GalleryListResult()
    object AccountMenuClickedResult: GalleryListResult()
}