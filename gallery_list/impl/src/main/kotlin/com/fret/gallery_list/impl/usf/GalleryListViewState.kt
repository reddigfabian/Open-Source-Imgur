package com.fret.gallery_list.impl.usf

import androidx.paging.PagingData
import com.fret.gallery_list.impl.items.GalleryListItem

data class GalleryListViewState(
    val galleryListPagingData: PagingData<GalleryListItem>? = null
)