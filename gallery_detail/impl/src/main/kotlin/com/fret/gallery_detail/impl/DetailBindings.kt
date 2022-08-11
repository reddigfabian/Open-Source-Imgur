package com.fret.gallery_detail.impl

import com.fret.gallery_detail.impl.views.DetailFragment
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(DetailScope::class)
interface DetailBindings {
    fun inject(fragment: DetailFragment)
}