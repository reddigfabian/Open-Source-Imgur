package com.fret.gallery_list.impl

import com.fret.gallery_list.impl.views.ListFragment
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(ListScope::class)
interface ListBindings {
    fun inject(fragment: ListFragment)
}