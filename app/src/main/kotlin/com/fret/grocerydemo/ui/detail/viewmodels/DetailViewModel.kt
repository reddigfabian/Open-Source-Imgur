package com.fret.grocerydemo.ui.detail.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.fret.grocerydemo.R

class DetailViewModel constructor(
    application : Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    val text = savedStateHandle.get<String>(application.applicationContext.getString(R.string.nav_arg_detail))

}