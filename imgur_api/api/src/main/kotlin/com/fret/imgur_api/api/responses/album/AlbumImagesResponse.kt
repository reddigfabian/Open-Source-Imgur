package com.fret.imgur_api.api.responses.album

import com.fret.imgur_api.api.models.image.ImageModel

data class AlbumImagesResponse(
    val data : List<ImageModel>
)
