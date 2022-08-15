package com.fret.utils

import androidx.lifecycle.ViewModel

interface ViewModelFactoryExperimental<T: ViewModel>  {
    fun create(vararg args: Any?): T
}