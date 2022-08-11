package com.fret.gallery_list.impl

import androidx.lifecycle.ViewModel
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(ListScope::class)
interface LibUiBindingsExperimental {
    fun viewModelFactoriesExperimental(): Map<Class<out ViewModel>, ViewModelFactoryExperimental<*>>
}