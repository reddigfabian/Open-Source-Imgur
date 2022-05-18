package com.fret.grocerydemo.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.fret.grocerydemo.di.ApplicationComponent

fun Fragment.getAppComponent(): ApplicationComponent = (requireActivity().applicationContext as GroceryApplication).appComponent

inline fun <reified T : ViewModel> Fragment.lazyViewModel(
    noinline create: (stateHandle: SavedStateHandle) -> T
) = viewModels<T> {
    Factory(this, create)
}