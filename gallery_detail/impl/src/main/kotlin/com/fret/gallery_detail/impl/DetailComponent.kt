package com.fret.gallery_detail.impl

import com.fret.di.AppScope
import com.fret.di.SingleIn
import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.ContributesTo

// TODO: Consider if a subcomponent per module makes sense

@SingleIn(DetailScope::class)
@ContributesSubcomponent(scope = DetailScope::class, parentScope = AppScope::class)
interface DetailComponent {
    @ContributesSubcomponent.Factory
    interface Factory {
        fun create(): DetailComponent
    }

    @ContributesTo(AppScope::class)
    interface ParentBindings {
        fun detailComponentBuilder(): Factory
    }
}