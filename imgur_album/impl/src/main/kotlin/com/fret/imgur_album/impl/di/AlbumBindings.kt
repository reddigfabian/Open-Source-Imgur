package com.fret.imgur_album.impl.di

import com.fret.imgur_album.impl.views.AlbumFragment
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(AlbumScope::class)
interface AlbumBindings {
    fun inject(fragment: AlbumFragment)
}