package com.fret.imgur_gallery.impl.usf

import androidx.paging.PagingData
import com.fret.imgur_gallery.impl.items.GalleryItem
import com.fret.usf.UsfResult

sealed class GalleryResult: UsfResult {
    object ScreenLoadResult: GalleryResult()
    data class GalleryListItemClickedResult(val albumHash: String): GalleryResult()
    data class GalleryListPageLoadResult(val pagingData: PagingData<GalleryItem>): GalleryResult()
}