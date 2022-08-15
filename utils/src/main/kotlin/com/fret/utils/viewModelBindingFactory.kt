package com.fret.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

inline fun <reified VM : ViewModel> Fragment.bindingViewModelFactory(): Lazy<VM> = lazy {
    (bindings<LibUiBindings>().viewModelFactories()[VM::class.java] as ViewModelFactory<VM, String>).create("Test")
}

inline fun <reified VM : ViewModel> Fragment.bindingViewModelFactory(noinline stringProducer : (() -> String?)): Lazy<VM> = lazy {
    (bindings<LibUiBindings>().viewModelFactories()[VM::class.java] as ViewModelFactory<VM, String?>).create(stringProducer.invoke())
}

inline fun <reified VM : ViewModel> Fragment.bindingViewModelFactoryB(stringProducer : Lazy<String>): Lazy<VM> = lazy {
    val t: String by stringProducer
    (bindings<LibUiBindings>().viewModelFactories()[VM::class.java] as ViewModelFactory<VM, String?>).create(t)
}

inline fun <reified VM : ViewModel> Fragment.bindingViewModelFactory(args: Bundle?): Lazy<VM> = lazy {
    (bindings<LibUiBindings>().viewModelFactories()[VM::class.java] as ViewModelFactory<VM, Bundle?>).create(args)
}