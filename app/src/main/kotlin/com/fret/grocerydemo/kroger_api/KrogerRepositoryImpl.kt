package com.fret.grocerydemo.kroger_api

import com.fret.grocerydemo.di.AppScope
import com.fret.grocerydemo.kroger_api.responses.KrogerProductResponse
import com.fret.grocerydemo.kroger_api.responses.KrogerProductResponseImpl
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(scope = AppScope::class, boundType = KrogerRepository::class)
class KrogerRepositoryImpl @Inject constructor(): KrogerRepository {
    override fun getItems(pageSize: Int, page : Int): KrogerProductResponse {
        return KrogerProductResponseImpl(DataFaker.generateFakeData(pageSize, page))
    }
}