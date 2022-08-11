package com.fret.gallery_list.impl.views

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import com.fret.gallery_list.impl.LibUiBindings
import com.fret.gallery_list.impl.ViewModelFactoryPlugin
import com.fret.utils.bindings

inline fun <reified VM : ViewModel> Fragment.bindingViewModelFactory(fragment: Fragment): Lazy<VM> = lazy {
    (bindings<LibUiBindings>().viewModelFactories()[VM::class.java] as ViewModelFactoryPlugin<VM, String>).create("Test")
}