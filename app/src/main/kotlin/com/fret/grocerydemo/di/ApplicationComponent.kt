package com.fret.grocerydemo.di

import com.fret.grocerydemo.ui.list.viewmodels.ListViewModel
import com.squareup.anvil.annotations.MergeComponent
import dagger.Component

@MergeComponent(ApplicationScope::class)
interface ApplicationComponent {
//    fun listViewModel(): ListViewModel.Factory
}