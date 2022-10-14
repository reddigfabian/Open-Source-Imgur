package com.fret.imgur_gallery.impl.di

import com.fret.imgur_gallery.impl.views.GalleryFragment
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(GalleryScope::class)
interface GalleryBindings {
    fun inject(fragment: GalleryFragment)
}