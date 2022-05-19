package com.fret.grocerydemo.ui.list.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.grocerydemo.kroger_api.paging.KrogerProductPagingSource
import com.fret.grocerydemo.ui.list.items.ListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tangle.viewmodel.VMInject

class ListViewModel @VMInject constructor(
    krogerProductPagingSource: KrogerProductPagingSource
) : ViewModel() {

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