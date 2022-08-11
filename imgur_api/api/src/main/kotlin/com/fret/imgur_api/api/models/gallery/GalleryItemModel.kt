package com.fret.imgur_api.api.models.gallery

data class GalleryItemModel(
    val title : String?,
    val cover: String?,
    val score: Int = 0,
    val comment_count: Int = 0,
    val views: Int = 0
)