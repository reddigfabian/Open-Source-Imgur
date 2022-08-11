package com.fret.gallery_list.impl

import androidx.lifecycle.ViewModel
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(ListScope::class)
interface LibUiBindings {
    fun viewModelFactories(): Map<Class<out ViewModel>, ViewModelFactoryPlugin<ViewModel>>
}