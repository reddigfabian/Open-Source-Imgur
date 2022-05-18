package com.fret.grocerydemo.di

import com.fret.grocerydemo.ui.list.viewmodels.ListViewModel
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun listViewModel(): ListViewModel.Factory
}