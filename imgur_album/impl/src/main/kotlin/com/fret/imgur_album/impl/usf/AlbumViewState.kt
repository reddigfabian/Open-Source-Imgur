package com.fret.imgur_album.impl.usf

import com.fret.imgur_album.impl.items.ImageListItem

data class AlbumViewState(
    val errorMessage: String? = null,
    val imageListItems: List<ImageListItem>? = null
)