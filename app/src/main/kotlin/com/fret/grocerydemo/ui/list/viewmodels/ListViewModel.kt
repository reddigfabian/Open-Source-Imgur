package com.fret.grocerydemo.ui.list.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.grocerydemo.kroger_api.KrogerRepository
import com.fret.grocerydemo.ui.list.items.ListItem
import com.fret.grocerydemo.ui.list.paging.KrogerProductPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListViewModel(private val krogerRepository: KrogerRepository) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class Factory(private val krogerRepository: KrogerRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListViewModel(krogerRepository) as T
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }

    private val pagingSourceFactory = InvalidatingPagingSourceFactory {
       KrogerProductPagingSource(krogerRepository, PAGE_SIZE)
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