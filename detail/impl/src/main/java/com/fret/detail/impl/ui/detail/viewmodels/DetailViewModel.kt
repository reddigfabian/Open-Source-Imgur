package com.fret.detail.impl.ui.detail.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.fret.kroger_api.api.repositories.KrogerRepository
import tangle.viewmodel.VMInject

class DetailViewModel @VMInject constructor(
    application : Application,
    savedStateHandle: SavedStateHandle,
    private val krogerRepository : KrogerRepository
) : AndroidViewModel(application) {

    val text = savedStateHandle.get<String>(application.applicationContext.getString(com.fret.detial.api.R.string.nav_arg_detail))

}