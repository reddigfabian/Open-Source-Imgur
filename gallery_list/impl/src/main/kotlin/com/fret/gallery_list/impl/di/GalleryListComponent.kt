package com.fret.gallery_list.impl.di

import com.fret.di.AppScope
import com.fret.di.SingleIn
import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.ContributesTo

@SingleIn(GalleryListScope::class)
@ContributesSubcomponent(scope = GalleryListScope::class, parentScope = AppScope::class)
interface GalleryListComponent {
    @ContributesSubcomponent.Factory
    interface Factory {
        fun create(): GalleryListComponent
    }

    @ContributesTo(AppScope::class)
    interface ParentBindings {
        fun listComponentBuilder(): Factory
    }
}