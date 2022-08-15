package com.fret.imgur_album.impl.usf

import com.fret.imgur_album.impl.items.ImageListItem

sealed class AlbumResult {
    data class ScreenLoadResult(val error: String? = null, val imageList: List<ImageListItem>? = null): AlbumResult()
    object AccountMenuClickedResult: AlbumResult()
}