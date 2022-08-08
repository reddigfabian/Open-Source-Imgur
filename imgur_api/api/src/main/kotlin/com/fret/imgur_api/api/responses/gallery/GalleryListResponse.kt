package com.fret.imgur_api.api.responses.gallery

import com.fret.imgur_api.api.models.gallery.GalleryItemModel

data class GalleryListResponse(
    val data : List<GalleryItemModel>
)
