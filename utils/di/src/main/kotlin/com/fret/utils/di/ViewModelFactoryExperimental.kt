package com.fret.utils.di

import androidx.lifecycle.ViewModel

interface ViewModelFactoryExperimental<T: ViewModel>  {
    fun create(vararg args: Any?): T
}