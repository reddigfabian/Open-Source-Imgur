package com.fret.grocerydemo.ui.list.viewmodels

import com.fret.grocerydemo.ui.list.items.ListItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.grocerydemo.kroger_api.KrogerRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListViewModel : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 30
    }

    private val pagingSourceFactory = InvalidatingPagingSourceFactory {
        KrogerRepositoryImpl.getItemsPagingSource(PAGE_SIZE)
    }

    val items: Flow<PagingData<ListItem>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true
        ), pagingSourceFactory = pagingSourceFactory
    ).flow
        .map { pagingData ->
            pagingData.map { string -> ListItem(string) }
        }
        .cachedIn(viewModelScope)
}