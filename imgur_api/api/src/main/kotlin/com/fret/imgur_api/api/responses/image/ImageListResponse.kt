package com.fret.imgur_api.api.responses.image

import com.fret.imgur_api.api.models.image.ImageItemModel

data class ImageListResponse(
    val data : List<ImageItemModel>
)