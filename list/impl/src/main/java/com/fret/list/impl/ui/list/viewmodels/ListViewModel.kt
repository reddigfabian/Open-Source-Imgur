package com.fret.list.impl.ui.list.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.list.impl.ui.list.items.ListItem
import com.fret.list.impl.ui.list.paging.KrogerProductPagingSource
import com.fret.kroger_api.api.repositories.KrogerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tangle.viewmodel.VMInject

class ListViewModel @VMInject constructor(
    private val krogerRepository : KrogerRepository
) : ViewModel() {

    private val pagingSourceFactory = InvalidatingPagingSourceFactory {
        KrogerProductPagingSource(krogerRepository)
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