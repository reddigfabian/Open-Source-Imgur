package com.fret.grocerydemo.ui.list.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.fret.grocerydemo.ui.list.items.ListItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.grocerydemo.kroger_api.KrogerRepository
import com.fret.grocerydemo.ui.list.paging.KrogerProductPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListViewModel private constructor(
//    private val savedStateHandle: SavedStateHandle,
    private val krogerRepository: KrogerRepository
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    class Factory(private val krogerRepository: KrogerRepository) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListViewModel(krogerRepository) as T
        }
    }

    private val pagingSourceFactory = InvalidatingPagingSourceFactory {
        KrogerProductPagingSource(PAGE_SIZE, krogerRepository)
    }

    val items: Flow<PagingData<ListItem>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true
        ), pagingSourceFactory = pagingSourceFactory
    ).flow
        .map { pagingData ->
            pagingData.map { productModel ->
                ListItem(productModel.description)
            }
        }
        .cachedIn(viewModelScope)
}