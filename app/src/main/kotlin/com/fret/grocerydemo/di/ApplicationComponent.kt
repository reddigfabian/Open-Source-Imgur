package com.fret.grocerydemo.di

import com.squareup.anvil.annotations.MergeComponent
import javax.inject.Singleton

@Singleton
@MergeComponent(AppScope::class)
interface ApplicationComponent {
}