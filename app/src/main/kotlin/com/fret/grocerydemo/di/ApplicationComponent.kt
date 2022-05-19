package com.fret.grocerydemo.di

import com.squareup.anvil.annotations.MergeComponent

@MergeComponent(ApplicationScope::class)
interface ApplicationComponent {
//    fun listViewModel(): ListViewModel.Factory
}