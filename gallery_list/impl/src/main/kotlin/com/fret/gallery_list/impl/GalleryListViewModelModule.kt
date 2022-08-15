
@file:Suppress(
  "DEPRECATION",
  "OPT_IN_USAGE",
  "OPT_IN_USAGE_ERROR",
)

package com.fret.gallery_list.impl

import com.fret.di.AppScope
import com.fret.gallery_list.impl.viewmodels.GalleryListViewModel
import com.fret.utils.ViewModelFactory
import com.fret.utils.ViewModelFactoryExperimental
import com.fret.utils.ViewModelKey
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

//@Module
//@ContributesTo(AppScope::class)
//abstract class GalleryListViewModelModule {
//  @Binds
//  @IntoMap
//  @ViewModelKey(GalleryListViewModel::class)
//  abstract
//      fun bindGalleryListViewModelFactory(factory: GalleryListViewModelAssistedFactory):
//      ViewModelFactoryExperimental<GalleryListViewModel>
//}
