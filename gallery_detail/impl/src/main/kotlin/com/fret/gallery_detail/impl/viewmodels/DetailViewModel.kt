package com.fret.gallery_detail.impl.viewmodels

import androidx.lifecycle.ViewModel
import com.fret.di.AppScope
import com.fret.di.ContributesViewModel
import com.fret.gallery_detail.impl.DetailScope
import com.fret.imgur_api.api.ImgurRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

//@ContributesViewModel(DetailScope::class)
class DetailViewModel @AssistedInject constructor(
//    savedStateHandle: SavedStateHandle,
    @Assisted val title: String,
    private val imgurRepository: ImgurRepository
) : ViewModel() {

}