package com.fret.imgur_gallery.impl.usf

import androidx.paging.PagingData
import com.fret.imgur_gallery.impl.items.GalleryItem
import com.fret.usf.UsfEvent

sealed class GalleryEvent: UsfEvent {
    object ScreenLoadEvent: GalleryEvent()
    data class GalleryListItemPageLoadEvent(val pagingData: PagingData<GalleryItem>): GalleryEvent()
    data class GalleryListItemClickedEvent(val albumHash: String): GalleryEvent()
}