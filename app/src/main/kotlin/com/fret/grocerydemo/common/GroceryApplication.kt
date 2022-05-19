package com.fret.grocerydemo.common

import android.app.Application
import com.fret.grocerydemo.di.AppScope
import com.fret.grocerydemo.di.DaggerApplicationComponent
import tangle.inject.TangleGraph
import tangle.inject.TangleScope

@TangleScope(AppScope::class)
class GroceryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val component = DaggerApplicationComponent.create()
        TangleGraph.add(component)
        TangleGraph.inject(this)
    }
}