package com.fret.imgur_album.impl.viewmodels

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fret.imgur_album.impl.di.AlbumScope
import com.fret.utils.di.bindings
import com.squareup.anvil.annotations.ContributesTo
import java.lang.IllegalStateException

@ContributesTo(AlbumScope::class)
interface AlbumViewModel_Bindings {
    fun viewModelFactories(): Map<Class<out ViewModel>, AlbumViewModel_Factory_AssistedFactory>
}

internal fun Fragment.viewModelFactory(albumHashProducer: () -> String): Lazy<AlbumViewModel> = viewModels {
    bindings<AlbumViewModel_Bindings>().viewModelFactories()[AlbumViewModel::class.java]?.create(albumHashProducer.invoke())
        ?: throw IllegalStateException("Unable to locate AlbumViewModel_Factory_AssistedFactory")
}