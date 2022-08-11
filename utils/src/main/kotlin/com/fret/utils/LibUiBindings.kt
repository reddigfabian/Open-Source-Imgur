package com.fret.utils

import androidx.lifecycle.ViewModel
import com.fret.di.AppScope
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(AppScope::class)
interface LibUiBindings {
    fun viewModelFactories(): Map<Class<out ViewModel>, ViewModelFactory<*, *>>
}