package com.fret.gallery_list.impl.views

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.fret.gallery_list.impl.LibUiBindings
import com.fret.gallery_list.impl.LibUiBindingsExperimental
import com.fret.gallery_list.impl.ViewModelFactory
import com.fret.gallery_list.impl.ViewModelFactoryExperimental
import com.fret.utils.bindings

inline fun <reified VM : ViewModel> Fragment.bindingViewModelFactory(): Lazy<VM> = lazy {
    (bindings<LibUiBindings>().viewModelFactories()[VM::class.java] as ViewModelFactory<VM, String>).create("Test")
}

inline fun <reified VM : ViewModel> Fragment.bindingViewModelFactoryExperimental(): Lazy<VM> = lazy {
    (bindings<LibUiBindingsExperimental>().viewModelFactoriesExperimental()[VM::class.java] as ViewModelFactoryExperimental<VM>).create("Test")
}