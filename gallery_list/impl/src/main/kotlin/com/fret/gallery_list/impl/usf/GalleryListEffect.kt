package com.fret.gallery_list.impl.usf

import com.fret.usf.UsfEffect

sealed class GalleryListEffect: UsfEffect {
    data class GalleryListItemClicked(val albumHash: String): GalleryListEffect()
}