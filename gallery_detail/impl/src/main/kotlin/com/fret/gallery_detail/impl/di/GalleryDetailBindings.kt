package com.fret.gallery_detail.impl.di

import com.fret.gallery_detail.impl.views.GalleryDetailFragment
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(GalleryDetailScope::class)
interface GalleryDetailBindings {
    fun inject(fragment: GalleryDetailFragment)
}