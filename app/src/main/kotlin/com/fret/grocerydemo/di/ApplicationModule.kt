package com.fret.grocerydemo.di

import com.fret.grocerydemo.kroger_api.KrogerRepository
import com.fret.grocerydemo.kroger_api.KrogerRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationModule {
    @Binds abstract fun bindsKrogerRepository(krogerRepositoryImpl: KrogerRepositoryImpl) : KrogerRepository
}