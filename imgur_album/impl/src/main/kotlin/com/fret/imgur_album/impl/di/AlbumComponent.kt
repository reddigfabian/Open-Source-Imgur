package com.fret.imgur_album.impl.di

import com.fret.di.AppScope
import com.fret.di.SingleIn
import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.ContributesTo

@SingleIn(AlbumScope::class)
@ContributesSubcomponent(scope = AlbumScope::class, parentScope = AppScope::class)
interface AlbumComponent {
    @ContributesSubcomponent.Factory
    interface Factory {
        fun create(): AlbumComponent
    }

    @ContributesTo(AppScope::class)
    interface ParentBindings {
        fun detailComponentBuilder(): Factory
    }
}