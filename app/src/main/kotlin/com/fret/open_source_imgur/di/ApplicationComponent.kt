package com.fret.open_source_imgur.di

import android.app.Application
import com.fret.di.AppScope
import com.fret.di.SingleIn
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component


@SingleIn(AppScope::class)
@MergeComponent(AppScope::class)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withApplication(application: Application): Builder
        fun build(): ApplicationComponent
    }
}