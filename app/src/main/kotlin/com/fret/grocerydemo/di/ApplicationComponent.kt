package com.fret.grocerydemo.di

import android.app.Application
import com.fret.di.AppScope
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@MergeComponent(AppScope::class)
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            application: Application
        ): ApplicationComponent
    }
}