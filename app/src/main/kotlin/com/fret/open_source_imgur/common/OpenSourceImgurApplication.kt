package com.fret.open_source_imgur.common

import android.app.Application
import com.fret.open_source_imgur.di.DaggerApplicationComponent
import com.fret.utils.DaggerComponentOwner

class OpenSourceImgurApplication : Application(), DaggerComponentOwner {
    override lateinit var daggerComponent: Any

    override fun onCreate() {
        super.onCreate()
        daggerComponent = DaggerApplicationComponent.builder().withApplication(this).build()
    }
}