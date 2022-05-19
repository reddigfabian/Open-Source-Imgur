package com.fret.grocerydemo.common

import android.app.Application
import com.fret.di.AppScope
import com.fret.grocerydemo.di.DaggerApplicationComponent
import tangle.inject.TangleGraph
import tangle.inject.TangleScope

@TangleScope(AppScope::class)
class GroceryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val component = DaggerApplicationComponent.factory().create(this)
        TangleGraph.add(component)
        TangleGraph.inject(this)
    }
}