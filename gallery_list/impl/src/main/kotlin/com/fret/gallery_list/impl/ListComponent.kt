package com.fret.gallery_list.impl

import com.fret.di.AppScope
import com.fret.di.SingleIn
import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.ContributesTo

@SingleIn(ListScope::class)
@ContributesSubcomponent(scope = ListScope::class, parentScope = AppScope::class)
interface ListComponent {
    @ContributesSubcomponent.Factory
    interface Factory {
        fun create(): ListComponent
    }

    @ContributesTo(AppScope::class)
    interface ParentBindings {
        fun listComponentBuilder(): Factory
    }
}