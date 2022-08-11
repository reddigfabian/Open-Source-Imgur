package com.fret.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

inline fun <reified VM : ViewModel> Fragment.bindingViewModelFactory(initialState: String): Lazy<VM> = lazy {
    (bindings<LibUiBindings>().viewModelFactories()[VM::class.java] as ViewModelFactory<VM, String>).create(initialState)
}

//inline fun <reified VM : ViewModel> Fragment.bindingViewModelFactoryExperimental(initialState: String): Lazy<VM> = lazy {
//    (bindings<LibUiBindingsExperimental>().viewModelFactoriesExperimental()[VM::class.java] as ViewModelFactoryExperimental<VM>).create(initialState)
//}