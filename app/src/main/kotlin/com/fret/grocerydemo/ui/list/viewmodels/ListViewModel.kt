package com.fret.grocerydemo.ui.list.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.fret.grocerydemo.ui.list.items.ListItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fret.grocerydemo.kroger_api.KrogerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 30
    }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): ListViewModel
    }

    @Inject lateinit var krogerRepository : KrogerRepository

    private val pagingSourceFactory = InvalidatingPagingSourceFactory {
        krogerRepository.getItemsPagingSource(PAGE_SIZE)
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