package com.fret.grocerydemo.common

import android.app.Application
import com.fret.grocerydemo.di.DaggerApplicationComponent

class GroceryApplication : Application() {
    val appComponent = DaggerApplicationComponent.create()
}