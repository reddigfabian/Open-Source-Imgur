package com.fret.gallery_list.impl.items

data class GalleryListItem(
    val id: String,
    val title: String?,
    val coverUrl: String?,
    val score: Int,
    val comments: Int,
    val views: Int
)