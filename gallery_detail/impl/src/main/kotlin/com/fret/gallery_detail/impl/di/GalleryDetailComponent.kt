package com.fret.gallery_detail.impl.di

import com.fret.di.AppScope
import com.fret.di.SingleIn
import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.ContributesTo

@SingleIn(GalleryDetailScope::class)
@ContributesSubcomponent(scope = GalleryDetailScope::class, parentScope = AppScope::class)
interface GalleryDetailComponent {
    @ContributesSubcomponent.Factory
    interface Factory {
        fun create(): GalleryDetailComponent
    }

    @ContributesTo(AppScope::class)
    interface ParentBindings {
        fun detailComponentBuilder(): Factory
    }
}