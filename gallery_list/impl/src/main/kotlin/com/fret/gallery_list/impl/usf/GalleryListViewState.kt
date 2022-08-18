package com.fret.gallery_list.impl.usf

import androidx.paging.PagingData
import com.fret.gallery_list.impl.items.GalleryListItem
import com.fret.shared_menus.account.usf.AccountMenuViewState
import com.fret.usf.UsfViewState

data class GalleryListViewState(
    val galleryListPagingData: PagingData<GalleryListItem>? = null
): UsfViewState