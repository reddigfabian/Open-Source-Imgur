package com.fret.imgur_gallery.impl.usf

import androidx.paging.PagingData
import com.fret.imgur_gallery.impl.items.GalleryItem
import com.fret.usf.UsfViewState

data class GalleryViewState(
    val galleryListPagingData: PagingData<GalleryItem>? = null
): UsfViewState