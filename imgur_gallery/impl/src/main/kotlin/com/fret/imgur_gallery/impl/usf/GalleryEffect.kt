package com.fret.imgur_gallery.impl.usf

import com.fret.usf.UsfEffect

sealed class GalleryEffect: UsfEffect {
    data class GalleryListItemClicked(val albumHash: String): GalleryEffect()
}