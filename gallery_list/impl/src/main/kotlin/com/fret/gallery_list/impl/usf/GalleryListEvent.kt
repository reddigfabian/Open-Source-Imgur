package com.fret.gallery_list.impl.usf

import androidx.paging.PagingData
import com.fret.gallery_list.impl.items.GalleryListItem

sealed class GalleryListEvent {
    object ScreenLoadEvent: GalleryListEvent()
    data class GalleryListItemPageLoadEvent(val pagingData: PagingData<GalleryListItem>): GalleryListEvent()
    data class GalleryListItemClickedEvent(val albumHash: String): GalleryListEvent()
    object AccountMenuClickedEvent: GalleryListEvent()
}