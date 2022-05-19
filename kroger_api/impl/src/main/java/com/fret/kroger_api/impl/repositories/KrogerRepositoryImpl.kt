package com.fret.kroger_api.impl.repositories

import com.fret.di.AppScope
import com.fret.kroger_api.api.repositories.KrogerRepository
import com.fret.kroger_api.api.responses.KrogerProductResponse
import com.fret.kroger_api.impl.DataFaker
import com.fret.kroger_api.impl.responses.KrogerProductResponseImpl
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(scope = AppScope::class, boundType = KrogerRepository::class)
class KrogerRepositoryImpl @Inject constructor(): KrogerRepository {
    override fun getItems(pageSize: Int, page : Int): KrogerProductResponse {
        return KrogerProductResponseImpl(DataFaker.generateFakeData(pageSize, page))
    }
}