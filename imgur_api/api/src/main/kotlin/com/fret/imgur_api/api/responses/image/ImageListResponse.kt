package com.fret.imgur_api.api.responses.image

import com.fret.imgur_api.api.models.image.ImageModel

data class ImageListResponse(
    val data : List<ImageModel>
)