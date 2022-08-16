package com.fret.gallery_list.impl.usf

sealed class GalleryViewEffect {
    data class GalleryListItemClicked(val albumHash: String): GalleryViewEffect()
    object AccountMenuClickedEffect: GalleryViewEffect()
}