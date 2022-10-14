package com.fret.imgur_gallery.impl.items

data class GalleryItem(
    val id: String,
    val title: String?,
    val coverUrl: String?,
    val score: Int,
    val comments: Int,
    val views: Int
)