package com.fret.grocerydemo.ui.list.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.grocerydemo.di.ApplicationScope
import com.fret.grocerydemo.kroger_api.paging.KrogerProductPagingSource
import com.fret.grocerydemo.ui.list.items.ListItem
import com.squareup.anvil.annotations.ContributesBinding
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ContributesBinding(ApplicationScope::class)
class ListViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    krogerProductPagingSource: KrogerProductPagingSource
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): ListViewModel
    }

    private val pagingSourceFactory = InvalidatingPagingSourceFactory {
        krogerProductPagingSource
    }

    val items: Flow<PagingData<ListItem>> = Pager(
        config = PagingConfig(
            pageSize = KrogerProductPagingSource.PAGE_SIZE,
            enablePlaceholders = true
        ), pagingSourceFactory = pagingSourceFactory
    ).flow
        .map { pagingData ->
            pagingData.map { string -> ListItem(string) }
        }
        .cachedIn(viewModelScope)
}