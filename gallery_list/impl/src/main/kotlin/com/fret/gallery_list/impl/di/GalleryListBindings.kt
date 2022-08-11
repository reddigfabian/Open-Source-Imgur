package com.fret.gallery_list.impl.di

import com.fret.gallery_list.impl.views.GalleryListFragment
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(GalleryListScope::class)
interface GalleryListBindings {
    fun inject(fragment: GalleryListFragment)
}