package com.fret.imgur_gallery.impl.di

import com.fret.di.AppScope
import com.fret.di.SingleIn
import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.ContributesTo
import dagger.Reusable

@SingleIn(GalleryScope::class)
@ContributesSubcomponent(scope = GalleryScope::class, parentScope = AppScope::class)
interface GalleryComponent {
    @ContributesSubcomponent.Factory
    interface Factory {
        fun create(): GalleryComponent
    }

    @ContributesTo(AppScope::class)
    interface ParentBindings {
        fun galleryComponentBuilder(): Factory
    }
}